package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {

    private int accountId;
    private int userId;
    private BigDecimal balance;

    public int getAccountId() {
        return this.accountId;
    }

    public int getUserId() {
        return this.userId;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }
}
