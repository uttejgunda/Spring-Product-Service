package com.example.day180springprodservicedemo.services;

import com.example.day180springprodservicedemo.dto.FakeStoreRequestDto;
import com.example.day180springprodservicedemo.exceptions.DBNotFoundException;
import com.example.day180springprodservicedemo.exceptions.ProductNotFoundException;
import com.example.day180springprodservicedemo.models.Product;

import java.util.List;

public interface ProductService {
    Product getSingleProduct(String productId) throws ProductNotFoundException, DBNotFoundException;
    List<Product> getAllProducts();
    List<Product> searchProducts(String searchText);

    Product createProduct(Product product);
    Product createProduct(FakeStoreRequestDto product);

}
