package com.example.day180springprodservicedemo.controllers;

import com.example.day180springprodservicedemo.models.Product;
import com.example.day180springprodservicedemo.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/products/{id}")
    public Product getSingleProduct (@PathVariable("id") String productId) {
        Product product = productService.getSingleProduct(productId);
        return product;
    }

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        List<Product> allProducts = productService.getAllProducts();
        return allProducts;
    }

    @GetMapping("/search") // localhost:9001/search?text=hello
    public List<Product> searchProducts(@RequestParam String queryText) {
        List<Product> searchResults = productService.searchProducts(queryText);
        return searchResults;
    }


    @PostMapping("/products")
    public void createProduct(@RequestBody Product productDetails) {  // REQUEST BODY ANNOTATION RECEIVED THE REQUEST BODY
        Product savedProduct = productService.createProduct(productDetails);
    }
}
