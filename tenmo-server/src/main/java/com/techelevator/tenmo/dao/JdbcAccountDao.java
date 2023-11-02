package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {
    private final JdbcTemplate jdbcTemplate;
    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<String> getUsers() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM account a JOIN tenmo_user ON a.account_id;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                accounts.add(mapRowToAccount(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        List<String> usernames = new ArrayList<>();
        for(Account account: accounts){
            usernames.add(account.getUsername());
        }
        return usernames;

         }

    @Override
    public BigDecimal getBalance(int accountId){
        Account newAccount = null;
        // create account
        String sql = "SELECT * FROM account WHERE user_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
            if(results.next()){
                 newAccount = mapRowToAccount(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        System.out.println(newAccount.getBalance());
        return newAccount.getBalance();
    }


    @Override
    public BigDecimal addToBalance(BigDecimal amountToAdd, int id) {
        return null;
    }

    @Override
    public BigDecimal subtractFromBalance(BigDecimal amountToSubtract, int id) {
        return null;
    }

    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setUsername(rs.getString("username"));
        account.setAccountId(rs.getInt("account_id"));
        account.setUserId(rs.getInt("user_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        return account;
    }
}
