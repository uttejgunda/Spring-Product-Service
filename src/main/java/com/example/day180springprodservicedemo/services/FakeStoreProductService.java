package com.example.day180springprodservicedemo.services;

import com.example.day180springprodservicedemo.dto.ErrorResponseDto;
import com.example.day180springprodservicedemo.dto.FakeStorePostResponseDto;
import com.example.day180springprodservicedemo.dto.FakeStoreRequestDto;
import com.example.day180springprodservicedemo.dto.FakeStoreResponseDto;
import com.example.day180springprodservicedemo.exceptions.DBNotFoundException;
import com.example.day180springprodservicedemo.exceptions.ProductNotFoundException;
import com.example.day180springprodservicedemo.models.Category;
import com.example.day180springprodservicedemo.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@Qualifier("FakeStoreService")
//@Primary
public class FakeStoreProductService implements ProductService {

    @Autowired
    RestTemplate restTemplate;

// NO NEED BECAUSE WE IMPLEMENTED getProduct() method IN THE RESPONSE CLASS ITSELF
//    private Product convertResponseToProduct(FakeStoreResponseDto responseDto) {
//        Product product = new Product();
//        product.setId(responseDto.getId());
//        product.setName(responseDto.getTitle());
//        product.setPrice(responseDto.getPrice() * 1.0);
//        product.setDescription(responseDto.getDescription());
//        product.setImageUrl(responseDto.getImage());
//        Category category = new Category();
//        category.setName(responseDto.getCategory());
//        product.setCategory(category);
//        return product;
//    }

// NO NEED BECAUSE WE IMPLEMENTED getProduct() method IN THE RESPONSE CLASS ITSELF
//    private Product convertPostResponseToProduct(FakeStorePostResponseDto postResponseDto) {
//        Product product = new Product();
//        product.setId(postResponseDto.getId());
//        product.setName(postResponseDto.getTitle());
//        product.setPrice(postResponseDto.getPrice() * 1.0);
//        product.setDescription(postResponseDto.getDescription());
//        product.setImageUrl(postResponseDto.getImage());
//        Category category = new Category();
//        category.setName(postResponseDto.getCategory());
//        product.setCategory(category);
            // ...
            // ...
//        return product;
//    }

    @Override
    public Product getSingleProduct(String productId) throws ProductNotFoundException, DBNotFoundException {
        FakeStoreResponseDto response = restTemplate.getForObject("https://fakestoreapi.com/products/"+productId, FakeStoreResponseDto.class );

        if (response == null) {
            throw new ProductNotFoundException("Product with id " + productId + " is not found...");
        }

//        connectToDB();

        Product product = response.toProduct();
        return product;
    }


    private void connectToDB() throws DBNotFoundException {
        throw new DBNotFoundException("DB not found...");
    }

    @Override
    public List<Product> getAllProducts() {
        FakeStoreResponseDto[] responseArray = restTemplate.getForObject("https://fakestoreapi.com/products/", FakeStoreResponseDto[].class );


        List<Product> productsList = new ArrayList<>();

        for(FakeStoreResponseDto response: responseArray) {
            Product product = response.toProduct();
            productsList.add(product);
        }

        return productsList;
    }

    @Override
    public List<Product> searchProducts(String searchText) {
        return null;
    }

    public Product createProduct(Product product) {
        return null;
    }

    @Override
    public Product createProduct(FakeStoreRequestDto fakeStoreRequestDto) {
        FakeStorePostResponseDto savedProduct = restTemplate.postForObject("https://fakestoreapi.com/products", fakeStoreRequestDto, FakeStorePostResponseDto.class);

        Product product = savedProduct.toProduct();
        return product;

    }
}
