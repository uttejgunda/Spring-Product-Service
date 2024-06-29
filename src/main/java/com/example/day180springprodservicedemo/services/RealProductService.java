package com.example.day180springprodservicedemo.services;

import com.example.day180springprodservicedemo.models.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RealProductService implements ProductService {
    @Override
    public Product getSingleProduct(String productId) {
        return null;
    }

    @Override
    public List<Product> getAllProducts() {
        return null;
    }

    @Override
    public List<Product> searchProducts(String searchText) {
        return null;
    }

    @Override
    public Product createProduct(Product product) {
        return null;
    }
}
