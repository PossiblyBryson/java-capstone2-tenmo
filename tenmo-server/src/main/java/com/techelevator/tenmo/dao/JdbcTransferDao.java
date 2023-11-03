package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.techelevator.tenmo.exception.TransferNotFoundException;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
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


    @Override
    public List<Transfer> getAllTransfers(int userId) {
        List<Transfer> list = new ArrayList<>();
        String sql = "SELECT t.transfer_id, t.amount, t.transfer_type_id, t.transfer_status_id, " +
                "CASE WHEN t.account_from = a.account_id THEN 'Sent' ELSE 'Received' END AS transfer_direction " +
                "FROM transfer t " +
                "JOIN account a ON t.account_from = a.account_id OR t.account_to = a.account_id " +
                "WHERE a.user_id = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            list.add(transfer);
        }
        return list;
    }


    @Override
    public Transfer getTransferById(int transactionId) {
        Transfer transfer = new Transfer();
        String sql = "SELECT t.*, u.username AS userFrom, v.username AS userTo, ts.transfer_status_desc, tt.transfer_type_desc FROM transfers t " +
                "JOIN accounts a ON t.account_from = a.account_id " +
                "JOIN accounts b ON t.account_to = b.account_id " +
                "JOIN users u ON a.user_id = u.user_id " +
                "JOIN users v ON b.user_id = v.user_id " +
                "JOIN transfer_statuses ts ON t.transfer_status_id = ts.transfer_status_id " +
                "JOIN transfer_types tt ON t.transfer_type_id = tt.transfer_type_id " +
                "WHERE t.transfer_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transactionId);
        if (results.next()) {
            transfer = mapRowToTransfer(results);
        } else {
            throw new TransferNotFoundException();
        }
        return transfer;
    }

    @Override
    public String sendTransfer(int userFrom, int userTo, BigDecimal amount) {
        return null;
    }

    @Override
    public String requestTransfer(int userFrom, int userTo, BigDecimal amount) {
        return null;
    }

    @Override
    public List<Transfer> getPendingRequests(int userId) {
        return null;
    }

    @Override
    public String updateTransferRequest(Transfer transfer, int statusId) {
        String responseMessage;
        if (statusId == 3) {
            responseMessage = updateTransferStatusOnly(transfer, statusId);
        } else {
            responseMessage = updateTransferAndBalance(transfer, statusId);
        }
        return responseMessage;
    }

    private String updateTransferStatusOnly(Transfer transfer, int statusId) {
        String sql = "UPDATE transfers SET transfer_status_id = ? WHERE transfer_id = ?;";
        jdbcTemplate.update(sql, statusId, transfer.getTransferId());
        return "Update successful";
    }

    private String updateTransferAndBalance(Transfer transfer, int statusId) {
//        BigDecimal transferAmount = transfer.getAmount();
//        if (accountDAO.getBalance(transfer.getAccountFrom()).compareTo(transferAmount) >= 0) {
//            String sql = "UPDATE transfers SET transfer_status_id = ? WHERE transfer_id = ?;";
//            jdbcTemplate.update(sql, statusId, transfer.getTransferId());
//            accountDAO.addToBalance(transferAmount, transfer.getAccountTo());
//            accountDA.subtractFromBalance(transferAmount, transfer.getAccountFrom());
//            return "Update successful";
//        } else {
//            return "Insufficient funds for transfer";
//        }
        return null;
    }

    private Transfer mapRowToTransfer(SqlRowSet results) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(results.getInt("transfer_id"));
        transfer.setTransferTypeId(results.getInt("transfer_type_id"));
        return transfer;
    }
}

