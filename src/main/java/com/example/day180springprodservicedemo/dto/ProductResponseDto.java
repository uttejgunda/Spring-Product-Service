package com.example.day180springprodservicedemo.dto;

import com.example.day180springprodservicedemo.models.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponseDto {
    private Product product;
    private String responseMsg;
}
