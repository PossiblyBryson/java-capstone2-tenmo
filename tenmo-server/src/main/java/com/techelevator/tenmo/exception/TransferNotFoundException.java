package com.techelevator.tenmo.exception;

public class TransferNotFoundException extends RuntimeException {
    public TransferNotFoundException() {
        super("Transfer not found");
    }
}
