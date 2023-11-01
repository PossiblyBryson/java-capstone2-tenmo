package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {

    private int id;
    private BigDecimal balance = new BigDecimal(0);

    public Account(AuthenticatedUser authUser) {
        this.authUser = authUser;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

}
