package com.techelevator.tenmo.controller;

import java.util.List;

import com.techelevator.tenmo.dao.AccountDao;
import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/transfers")
public class TransferController {

    private final TransferDao transferDao;
    private final AccountDao accountDao;

    @Autowired
    public TransferController(TransferDao transfersDAO, AccountDao accountDao) {
        this.transferDao = transfersDAO;
        this.accountDao = accountDao;
    }

    @RequestMapping(path = "/account/{id}", method = RequestMethod.GET)
    public List<Transfer> listAllTransfersForUser(@PathVariable int id) {
        return transferDao.getAllTransfers(id);
    }

    @GetMapping("/{id}")
    public Transfer getTransferById(@PathVariable int id) {
        return transferDao.getTransferById(id);
    }

    @RequestMapping(path = "/request", method = RequestMethod.POST)
    public boolean requestTransfer(@RequestBody Transfer transfer) {
        return transferDao.requestTransfer(transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
    }

    @RequestMapping(path = "/requests/{id}", method = RequestMethod.GET)
    public List<Transfer> listPendingTransferRequests(@PathVariable int id) {
        return transferDao.getPendingRequests(id);
    }

//    @PutMapping("/update/{statusId}")
//    public String updateTransferStatus(@RequestBody Transfer transfer, @PathVariable int statusId) {
//        return transferDao.updateTransferRequest(transfer, statusId);
//    }

    @RequestMapping(path = "/requests/{id}/accept", method = RequestMethod.PUT)
    public boolean acceptTransfer(@PathVariable int id) {
        return accountDao.acceptRequest(id);
    }
}
