package com.example.day180springprodservicedemo.controllers;

import com.example.day180springprodservicedemo.models.Product;
import com.example.day180springprodservicedemo.services.ProductService;
import com.example.day180springprodservicedemo.services.RealProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HelloController {

    @Autowired
    @Qualifier("RealService")
    ProductService productService;
    @GetMapping("/hello/{name}")
    public String sayGreeting(@PathVariable("name") String name) {
        List<Product> myProducts = productService.getAllProducts();
        return "Hey " + name + "... How are you?" + "The stock count is: " + myProducts.size();
    }
}
