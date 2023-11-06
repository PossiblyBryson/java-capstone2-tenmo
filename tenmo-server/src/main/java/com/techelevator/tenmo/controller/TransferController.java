package com.techelevator.tenmo.controller;

import java.util.List;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.TransferDto;
import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;

@RestController
@RequestMapping("/transfer")
public class TransferController {

    private final TransferDao transferDao;
    private final AccountDao accountDao;

    @Autowired
    public TransferController(TransferDao transfersDAO, AccountDao accountDao) {
        this.transferDao = transfersDAO;
        this.accountDao = accountDao;
    }

    //Uses userId
    @RequestMapping(path = "/account/{id}", method = RequestMethod.GET)
    public List<Transfer> listAllTransfersForUser(@PathVariable int id) {
        return transferDao.getAllTransfers(id);
    }
    //Uses transferId
    @GetMapping("/{id}")
    public Transfer getTransferById(@PathVariable int id) {
        return transferDao.getTransferById(id);
    }

    @RequestMapping(path = "/request", method = RequestMethod.POST)
    public boolean requestTransfer(@RequestBody Transfer transfer) {
        return transferDao.requestTransfer(transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
    }

    @RequestMapping(path = "/request/{id}", method = RequestMethod.GET)
    public List<Transfer> listPendingTransferRequests(@PathVariable int id) {
        return transferDao.getPendingRequests(id);
    }

    @RequestMapping(path = "/request/{id}/accept", method = RequestMethod.PUT)
    public boolean acceptTransfer(@PathVariable int id) {
        return transferDao.acceptRequest(id);
    }

    @RequestMapping(path = "/request/{id}/deny", method = RequestMethod.PUT)
    public boolean denyTransfer(@PathVariable int id) {
        return transferDao.denyRequest(id);
    }

    //Path uses userId
    @RequestMapping(path = "/{id}" + "/send", method = RequestMethod.POST)
    public ResponseEntity<Boolean> sendTEBucks(@RequestBody TransferDto transfers) {
        boolean isUpdated = transferDao.sendTEBucks(transfers.getAmount(), transfers.getAccountTo(), transfers.getAccountFrom());
        if(isUpdated){
            // get balance of current user
            return new ResponseEntity<Boolean>(isUpdated, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
