package com.example.errors;
public class BadRequestAlertException extends Exception {
    public BadRequestAlertException(String message) {
        super(message);
    }
}
