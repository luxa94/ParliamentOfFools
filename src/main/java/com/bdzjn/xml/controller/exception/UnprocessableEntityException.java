package com.bdzjn.xml.controller.exception;

public class UnprocessableEntityException extends RuntimeException {

    public UnprocessableEntityException() {
    }

    public UnprocessableEntityException(String message) {
        super(message);
    }
}
