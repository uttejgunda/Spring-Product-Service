package com.example.day180springprodservicedemo.exceptions;

import com.example.day180springprodservicedemo.dto.ProductResponseDto;

public class ProductNotFoundException extends Exception{
    public ProductNotFoundException(String message) {
        super(message);
    }
}
