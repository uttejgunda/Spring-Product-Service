package com.example.day180springprodservicedemo.exceptions;

public class DBNotFoundException extends Exception {
    public DBNotFoundException(String message) {
        super(message);
    }
}
