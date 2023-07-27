package com.example.paymentgateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class InsufficientAmountException extends ResponseStatusException {
    public InsufficientAmountException() {
        super(HttpStatus.BAD_REQUEST,"Insufficient Amount");
    }
}
