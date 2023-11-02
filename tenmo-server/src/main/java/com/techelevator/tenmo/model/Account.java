package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {
    private String username;
    private int accountId;
    private int userId;
    private BigDecimal balance;

    //username setter and getter
    public void setUsername(String username){
        this.username = username;
    }
    public String getUsername(){
        return this.username;
    }

    //accountId setter and getter
    public void setAccountId(int id){
        this.accountId = id;
    }
    public int getAccountId(){
        return this.accountId;
    }

    //UserId setter and getter
    public void setUserId(int id){
        this.userId = id;
    }
    public int getUserId(){
        return this.userId;
    }

    //Balance setter and getter
    public void setBalance(BigDecimal balance){
        this.balance = balance;
    }
    public BigDecimal getBalance(){
        return this.balance;
    }


}
