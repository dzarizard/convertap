package com.pawlik.convertap.exception;

public class AccountUpdateException extends RuntimeException {
    public AccountUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}