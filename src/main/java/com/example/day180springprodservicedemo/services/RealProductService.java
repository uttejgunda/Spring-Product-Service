package com.example.day180springprodservicedemo.services;

import com.example.day180springprodservicedemo.dto.FakeStoreRequestDto;
import com.example.day180springprodservicedemo.models.Product;
import com.example.day180springprodservicedemo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Qualifier("RealService")
public class RealProductService implements ProductService {

    @Autowired
    ProductRepository productRepository;
    @Override
    public Product getSingleProduct(String productId) {
        return null;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.getAllProducts();
    }

    @Override
    public List<Product> searchProducts(String searchText) {
        List<Product> productsFromDB = productRepository.getAllProducts();

        // DOING THE FILTERING BASED ON SEARCH TEXT
        List<Product> result = new ArrayList<>();

        // Name of product contains search text
        for(Product product : productsFromDB) {
            if(product.getName().contains(searchText)) {
                result.add(product);
            }
        }
        return result;
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product createProduct(FakeStoreRequestDto product) {
        return null;
    }
}
