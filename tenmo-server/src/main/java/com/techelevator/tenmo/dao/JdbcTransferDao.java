package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.exception.TransferNotFoundException;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import com.techelevator.tenmo.model.Transfer;

@Component
public class JdbcTransferDao implements TransferDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private AccountDao accountDAO;

    private static int PENDING = 1;
    private static int APPROVED =2;
    private static int REJECTED = 3;

    private static int REQUEST = 1;
    private static int SEND = 2;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transfer> getAllTransfers(int userId) {
        List<Transfer> list = new ArrayList<>();
        String sql = "SELECT * FROM transfer WHERE account_to = ? OR account_from = ? ";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountDAO.getAccountIdFromUserId(userId), accountDAO.getAccountIdFromUserId(userId));
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            list.add(transfer);
        }
        return list;
    }


    @Override
    public Transfer getTransferById(int transactionId) {
        Transfer transfer = new Transfer();
        String sql = "SELECT * FROM transfer WHERE transfer_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transactionId);
        if (results.next()) {
            transfer = mapRowToTransfer(results);
        } else {
            throw new TransferNotFoundException();
        }
        return transfer;
    }

    @Override
    public boolean requestTransfer(int accountFrom, int accountTo, BigDecimal amount) {
        boolean success = false;
        if (accountFrom == accountTo) {
            throw new DaoException("Request failed: not a valid amount");
        }
        if (amount.equals(new BigDecimal(0))) {
            throw new DaoException("Cannot send an amount of 0");
        }
        if (amount.compareTo(BigDecimal.valueOf(0)) < 0) {
            throw new DaoException("Cannot send a negative amount");
        }
        BigDecimal balanceAvailable = accountDAO.getBalance(accountFrom);
        if (amount.compareTo(balanceAvailable) > 0) {
            throw new DaoException("Cannot send more money than is in your account");
        }
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, " +
                "account_to, amount) VALUES (1,1,?,?,?)";
        int rowsAffected = jdbcTemplate.update(sql, accountFrom, accountTo, amount);
        if (rowsAffected == 1) {
            success = true;
        }
        return success;
    }

    @Override
    public List<Transfer> getPendingRequests(int accountTo) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT * FROM transfer WHERE account_to = ? " +
                "AND transfer_status_id = 1 AND transfer_type_id = 1";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountTo);
        while (results.next()) {
            transfers.add(mapRowToTransfer(results));
        }
        return transfers;
    }

    @Override
    public boolean sendTEBucks(BigDecimal amountToAdd, int recepientId, int senderId) {
        boolean didItWork = false;
        if (recepientId == senderId) {
            throw new DaoException("Cannot send money to own account");
        }
        if (amountToAdd.equals(new BigDecimal(0))) {
            throw new DaoException("Cannot send an amount of 0");
        }
        if (amountToAdd.compareTo(BigDecimal.valueOf(0)) < 0) {
            throw new DaoException("Cannot send a negative amount");
        }
        BigDecimal balanceAvailable = accountDAO.getBalance(accountDAO.getAccountIdFromUserId(senderId));
        if (amountToAdd.compareTo(balanceAvailable) > 0) {
            throw new DaoException("Cannot send more money than is in your account");
        }
        String insertTransferSql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?, ?, ?, ?, ?)";
        String sql = "UPDATE account SET balance = balance + ? " +
                "WHERE user_id = ?";
        String sql1 = "UPDATE account SET balance = balance - ? " + "" +
                "WHERE user_id =?";
        //create a record in the transfers table

        //TODO:create a record in the transfers table
        int transferTypeId = SEND;
        int transferStatusId = APPROVED;

        try {
            int insertResults = jdbcTemplate.update(insertTransferSql, transferTypeId, transferStatusId, accountDAO.getAccountIdFromUserId(senderId), accountDAO.getAccountIdFromUserId(recepientId), amountToAdd);
            int results = jdbcTemplate.update(sql, amountToAdd, recepientId);
            int results1 = jdbcTemplate.update(sql1, amountToAdd, senderId);
            if (insertResults == 1 && results == 1 && results1 == 1) {
                didItWork = true;
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return didItWork;
    }

    public boolean acceptRequest(int transferId) {
        boolean didItWork = false;
        String updateStatus = "UPDATE transfer SET transfer_status_id = " + APPROVED +
                " WHERE transfer_id = ?";
        String sqlAdd = "UPDATE account SET balance = balance + (SELECT amount FROM transfer WHERE transfer_id = ?) " +
                "WHERE account_id = (SELECT account_to FROM transfer WHERE transfer_id = ?)";
        String sqlSubtract = "UPDATE account SET balance = balance - (SELECT amount FROM transfer WHERE transfer_id = ?) " +
                "WHERE account_id = (SELECT account_from FROM transfer WHERE transfer_id = ?)";
        //create a record in the transfers table

        try {
            int updateStatusResults = jdbcTemplate.update(updateStatus, transferId);
            int addResult = jdbcTemplate.update(sqlAdd, transferId, transferId);
            int subtractResult = jdbcTemplate.update(sqlSubtract, transferId, transferId);
            if (updateStatusResults == 1 && addResult == 1 && subtractResult == 1) {
                didItWork = true;
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return didItWork;
    }

    public boolean denyRequest(int transferId) {
        boolean didItWork = false;
        String updateStatus = "UPDATE transfer SET transfer_status_id = " + REJECTED +
                " WHERE transfer_id = ?";
        try {
            int updateStatusResults = jdbcTemplate.update(updateStatus, transferId);
            if (updateStatusResults == 1) {
                didItWork = true;
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return didItWork;
    }

    private Transfer mapRowToTransfer(SqlRowSet results) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(results.getInt("transfer_id"));
        transfer.setTransferTypeId(results.getInt("transfer_type_id"));
        transfer.setTransferStatusId(results.getInt("transfer_status_id"));
        transfer.setAccountFrom(results.getInt("account_from"));
        transfer.setAccountTo(results.getInt("account_to"));
        transfer.setAmount(results.getBigDecimal("amount"));
        return transfer;
    }
}

