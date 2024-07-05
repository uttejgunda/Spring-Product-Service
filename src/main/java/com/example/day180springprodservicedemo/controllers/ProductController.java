package com.example.day180springprodservicedemo.controllers;

import com.example.day180springprodservicedemo.dto.ErrorResponseDto;
import com.example.day180springprodservicedemo.dto.FakeStoreRequestDto;
import com.example.day180springprodservicedemo.dto.ListProductsResponseDto;
import com.example.day180springprodservicedemo.dto.ProductResponseDto;
import com.example.day180springprodservicedemo.exceptions.DBNotFoundException;
import com.example.day180springprodservicedemo.exceptions.ProductNotFoundException;
import com.example.day180springprodservicedemo.models.Product;
import com.example.day180springprodservicedemo.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductController {

    @Autowired
    @Qualifier("FakeStoreService")
    ProductService productService;

    // getSingleProduct() BY USING TRY & CATCH EXPLOSION WAY OF HANDLING
//    @GetMapping("/products/{id}")
//    public ResponseEntity<ProductResponseDto> getSingleProduct (@PathVariable("id") String productId) {
//        try {
//            Product product = productService.getSingleProduct(productId);
//            ProductResponseDto productResponseDto = new ProductResponseDto();
//            productResponseDto.setProduct(product);
//            productResponseDto.setResponseMsg("SUCCESS");
//
//            ResponseEntity<ProductResponseDto> responseEntity = new ResponseEntity<>(productResponseDto, HttpStatus.OK);
//            return responseEntity;
//        } catch (ProductNotFoundException e) {
//            ProductResponseDto productResponseDto = new ProductResponseDto();
//            productResponseDto.setProduct(null);
//            productResponseDto.setResponseMsg(e.getMessage());
//            ResponseEntity<ProductResponseDto> responseEntity = new ResponseEntity<>(productResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
//            return responseEntity;
//        } catch (DBNotFoundException e) {
//            ProductResponseDto productResponseDto = new ProductResponseDto();
//            productResponseDto.setProduct(null);
//            productResponseDto.setResponseMsg(e.getMessage());
//            ResponseEntity<ProductResponseDto> responseEntity = new ResponseEntity<>(productResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
//            return responseEntity;
//        }
//    }


    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponseDto> getSingleProduct (@PathVariable("id") String productId) throws DBNotFoundException, ProductNotFoundException {
            Product product = productService.getSingleProduct(productId);
            ProductResponseDto productResponseDto = new ProductResponseDto();
            productResponseDto.setProduct(product);
            productResponseDto.setResponseMsg("SUCCESS");

            ResponseEntity<ProductResponseDto> responseEntity = new ResponseEntity<>(productResponseDto, HttpStatus.OK);
            return responseEntity;

    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleDBNotFoundException (ProductNotFoundException errorObject ) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage("From CONTROLLER: " + errorObject.getMessage());
        ResponseEntity<ErrorResponseDto> responseEntity = new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        return responseEntity;
    }


    // getAllProducts() BY USING TRY & CATCH EXPLOSION WAY OF HANDLING
//    @GetMapping("/products")
//    public ResponseEntity<ListProductsResponseDto> getAllProducts() {
//
//        try {
//            List<Product> allProducts = productService.getAllProducts();
//            ListProductsResponseDto responseDto = new ListProductsResponseDto();
//            responseDto.setProducts(allProducts);
//            responseDto.setResponseMsg("SUCCESS");
//            ResponseEntity responseEntity = new ResponseEntity(responseDto,HttpStatus.OK);
//            return responseEntity;
//        } catch (Exception e) {
//            ListProductsResponseDto responseDto = new ListProductsResponseDto();
//            responseDto.setProducts (new ArrayList<>());
//            responseDto.setResponseMsg("FAILURE");
//            ResponseEntity responseEntity = new ResponseEntity(responseDto,HttpStatus.INTERNAL_SERVER_ERROR);
//            return responseEntity;
//        }
//    }


    @GetMapping("/products")
    public ResponseEntity<ListProductsResponseDto> getAllProducts() {
            List<Product> allProducts = productService.getAllProducts();
            ListProductsResponseDto responseDto = new ListProductsResponseDto();
            responseDto.setProducts(allProducts);
            responseDto.setResponseMsg("SUCCESS");
            ResponseEntity responseEntity = new ResponseEntity(responseDto,HttpStatus.OK);
            return responseEntity;
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
