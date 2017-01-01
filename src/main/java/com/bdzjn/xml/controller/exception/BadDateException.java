package com.bdzjn.xml.controller.exception;

public class BadDateException extends RuntimeException {

    public BadDateException() {

    }

    public BadDateException(String message) {
        super(message);
    }
}
