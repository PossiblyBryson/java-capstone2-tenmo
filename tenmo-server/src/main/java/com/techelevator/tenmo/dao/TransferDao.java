package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;

import com.techelevator.tenmo.model.Transfer;

public interface TransferDao {

    public List<Transfer> getAllTransfers(int userId);

    public Transfer getTransferById(int transactionId);

    public boolean requestTransfer(int userFrom, int userTo, BigDecimal amount);

    public List<Transfer> getPendingRequests(int userId);

    public String updateTransferRequest(Transfer transfer, int statusId);
}
