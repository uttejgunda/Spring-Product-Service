package com.example.day180springprodservicedemo.services;

import com.example.day180springprodservicedemo.dto.FakeStoreRequestDto;
import com.example.day180springprodservicedemo.models.Product;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Qualifier("RealService")
public class RealProductService implements ProductService {
    @Override
    public Product getSingleProduct(String productId) {
        return null;
    }

    @Override
    public List<Product> getAllProducts() {
        return new ArrayList<>();
    }

    @Override
    public List<Product> searchProducts(String searchText) {
        return null;
    }

    @Override
    public Product createProduct(Product product) {
        return null;
    }

    public Product createProduct(FakeStoreRequestDto product) {
        return null;
    }
}
