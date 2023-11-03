package com.techelevator.tenmo.controller;

import java.util.List;
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

    @Autowired
    public TransferController(TransferDao transfersDAO) {
        this.transferDao = transfersDAO;
    }

    @GetMapping("/account/{id}")
    public List<Transfer> listAllTransfersForUser(@PathVariable int id) {
        return transferDao.getAllTransfers(id);
    }

    @GetMapping("/{id}")
    public Transfer getTransferById(@PathVariable int id) {
        return transferDao.getTransferById(id);
    }

    @PostMapping("/send")
    public String sendTransfer(@RequestBody Transfer transfer) {
        return transferDao.sendTransfer(transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
    }

    @PostMapping("/request")
    public String requestTransfer(@RequestBody Transfer transfer) {
        return transferDao.requestTransfer(transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
    }

    @GetMapping("/requests/{id}")
    public List<Transfer> listPendingTransferRequests(@PathVariable int id) {
        return transferDao.getPendingRequests(id);
    }

    @PutMapping("/update/{statusId}")
    public String updateTransferStatus(@RequestBody Transfer transfer, @PathVariable int statusId) {
        return transferDao.updateTransferRequest(transfer, statusId);
    }
}
