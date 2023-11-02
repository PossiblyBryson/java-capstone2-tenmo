package com.techelevator.tenmo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.techelevator.tenmo.dao.TransfersDAO;
import com.techelevator.tenmo.model.Transfers;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/transfers")
public class TransferController {

    private final TransfersDAO transfersDAO;

    @Autowired
    public TransferController(TransfersDAO transfersDAO) {
        this.transfersDAO = transfersDAO;
    }

    @GetMapping("/account/{id}")
    public List<Transfers> listAllTransfersForUser(@PathVariable int id) {
        return transfersDAO.getAllTransfers(id);
    }

    @GetMapping("/{id}")
    public Transfers getTransferById(@PathVariable int id) {
        return transfersDAO.getTransferById(id);
    }

    @PostMapping("/send")
    public String sendTransfer(@RequestBody Transfers transfer) {
        return transfersDAO.sendTransfer(transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
    }

    @PostMapping("/request")
    public String requestTransfer(@RequestBody Transfers transfer) {
        return transfersDAO.requestTransfer(transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
    }

    @GetMapping("/requests/{id}")
    public List<Transfers> listPendingTransferRequests(@PathVariable int id) {
        return transfersDAO.getPendingRequests(id);
    }

    @PutMapping("/update/{statusId}")
    public String updateTransferStatus(@RequestBody Transfers transfer, @PathVariable int statusId) {
        return transfersDAO.updateTransferRequest(transfer, statusId);
    }
}
