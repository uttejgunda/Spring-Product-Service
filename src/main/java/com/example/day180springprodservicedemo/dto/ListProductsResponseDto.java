package com.example.day180springprodservicedemo.dto;

import com.example.day180springprodservicedemo.models.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListProductsResponseDto {
    private List<Product> products;
    private String responseMsg;
}
