package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.dao.UserDao;
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

    @RequestMapping("/{userId}/balance")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable int userId) {
        BigDecimal balance = accountDao.getBalance(userId);
        if (balance != null) {
            return new ResponseEntity<>(balance, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @RequestMapping("/usernames")
    public ResponseEntity<List<String>> getUsernames(){
        List<String> usernames = userDao.getUsernames();
        if(usernames != null){
            return new ResponseEntity<>(usernames, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

}
