package com.zmzaba.banking.exception;

public class InsufficientFundsException extends RuntimeException{
    public InsufficientFundsException(String message){

        super(message);
    }
}