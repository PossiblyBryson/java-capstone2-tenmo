package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {


    public boolean sendTEBucks(BigDecimal amountToAdd, int recepientId, int senderId );
    BigDecimal getBalance(int accountId);
}
