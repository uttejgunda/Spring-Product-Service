package com.example.day180springprodservicedemo.repository;

import com.example.day180springprodservicedemo.models.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductRepository {

    public Product save(Product product) {
        // Connect to MySql DB
        // Execute the query - insert into products () values ()
        return null;
    }

    public List<Product> getAllProducts() {
        // Connect to MySql DB
        // Execute the query - select * from products
        return new ArrayList<>();
    }

}
