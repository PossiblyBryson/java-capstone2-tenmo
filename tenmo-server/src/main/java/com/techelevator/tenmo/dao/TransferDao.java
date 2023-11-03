package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;

import com.techelevator.tenmo.model.Transfer;

public interface TransferDao {

    List<Transfer> getAllTransfers(int userId);
    Transfer getTransferById(int transactionId);
    boolean requestTransfer(int userFrom, int userTo, BigDecimal amount);
    List<Transfer> getPendingRequests(int userId);
    boolean sendTEBucks(BigDecimal amountToAdd, int recepientId, int senderId);
    boolean acceptRequest(int transferId);
    boolean denyRequest(int transferId);

}
