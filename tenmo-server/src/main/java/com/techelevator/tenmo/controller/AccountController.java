package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.TransferDto;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final UserDao userDao;

    private final AccountDao accountDao;

    @Autowired
    public AccountController(AccountDao accountDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.userDao =  userDao;
    }

    @RequestMapping(path = "/{userId}/balance", method = RequestMethod.GET)
    public ResponseEntity<BigDecimal> getBalance(@PathVariable int userId) {
        BigDecimal balance = accountDao.getBalance(userId);
        if (balance != null) {
            return new ResponseEntity<>(balance, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public ResponseEntity<List<User>> getUsers(){
        List<User> users = userDao.getUsers();
        if (users != null) {
            return new ResponseEntity<>(users, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @RequestMapping(path = "/userById/{userId}", method = RequestMethod.GET)
    public ResponseEntity<User> getUserById(@PathVariable int userId){
        User user = userDao.getUserById(userId);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @RequestMapping(path = "/accountByUserId/{userId}", method = RequestMethod.GET)
    public ResponseEntity<Account> getAccountByUserId(@PathVariable int userId){
        Account account = accountDao.getAccountByUserId(userId);
        if (account != null) {
            return new ResponseEntity<>(account, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @RequestMapping(path = "/accountByAccountId/{accountId}", method = RequestMethod.GET)
    public ResponseEntity<Account> getAccountByAccountId(@PathVariable int accountId){
        Account account = accountDao.getAccountByAccountId(accountId);
        if (account != null) {
            return new ResponseEntity<>(account, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
