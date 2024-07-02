package com.example.day180springprodservicedemo.controllers;

import com.example.day180springprodservicedemo.dto.FakeStoreRequestDto;
import com.example.day180springprodservicedemo.dto.ProductResponseDto;
import com.example.day180springprodservicedemo.exceptions.DBNotFoundException;
import com.example.day180springprodservicedemo.exceptions.ProductNotFoundException;
import com.example.day180springprodservicedemo.models.Product;
import com.example.day180springprodservicedemo.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponseDto> getSingleProduct (@PathVariable("id") String productId) {
        try {
            Product product = productService.getSingleProduct(productId);
            ProductResponseDto productResponseDto = new ProductResponseDto();
            productResponseDto.setProduct(product);
            productResponseDto.setResponseMsg("SUCCESS");

            ResponseEntity<ProductResponseDto> responseEntity = new ResponseEntity<>(productResponseDto, HttpStatus.OK);
            return responseEntity;
        } catch (ProductNotFoundException e) {
            ProductResponseDto productResponseDto = new ProductResponseDto();
            productResponseDto.setProduct(null);
            productResponseDto.setResponseMsg(e.getMessage());
            ResponseEntity<ProductResponseDto> responseEntity = new ResponseEntity<>(productResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
            return responseEntity;
        } catch (DBNotFoundException e) {
            ProductResponseDto productResponseDto = new ProductResponseDto();
            productResponseDto.setProduct(null);
            productResponseDto.setResponseMsg(e.getMessage());
            ResponseEntity<ProductResponseDto> responseEntity = new ResponseEntity<>(productResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
            return responseEntity;
        }
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
    public Product createProduct(@RequestBody FakeStoreRequestDto productDetails) {  // REQUEST BODY ANNOTATION RECEIVED THE REQUEST BODY
        Product savedProduct = productService.createProduct(productDetails);
        return savedProduct;
    }
}
