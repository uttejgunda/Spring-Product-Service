package com.example.day180springprodservicedemo.services;

import com.example.day180springprodservicedemo.models.Product;

import java.util.List;

public interface ProductService {
    Product getSingleProduct(String productId);
    List<Product> getAllProducts();
    List<Product> searchProducts(String searchText);
    Product createProduct(Product product);

}
