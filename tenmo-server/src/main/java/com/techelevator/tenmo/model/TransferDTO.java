package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class TransferDTO {
    private BigDecimal amount;
    private int accountTo;
    private int accountFrom;

    public void setAmount(BigDecimal amount){
        this.amount = amount;
    }
    public BigDecimal getAmount(){
        return this.amount;
    }
    public void setAccountTo(int id){
        this.accountTo = id;
    }
    public int getAccountTo(){
        return this.accountTo;
    }
    public void setAccountFrom(int id){
        this.accountFrom = id;
    }
    public int getAccountFrom(){
        return this.accountFrom;
    }
}
