package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.TransferDTO;
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

    @RequestMapping(path = "/{id}" + "/transfer", method = RequestMethod.PUT)
    public ResponseEntity<Boolean> updateBalance(@RequestBody TransferDTO transfers) {
        boolean isUpdated = accountDao.sendTEBucks(transfers.getAmount(), transfers.getAccountTo(), transfers.getAccountFrom());
        if(isUpdated){
            // get balance of current user
            return new ResponseEntity<Boolean>(isUpdated, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
