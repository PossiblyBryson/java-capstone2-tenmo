package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {
    int getAccountIdFromUserId(int userId);

    BigDecimal getBalance(int accountId);
    boolean sendTEBucks(BigDecimal amountToAdd, int recepientId, int senderId );


}
