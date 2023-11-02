package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {



    BigDecimal getBalance(int accountId);
    BigDecimal addToBalance(BigDecimal amountToAdd, int id);
    BigDecimal subtractFromBalance(BigDecimal amountToSubtract, int id);
}
