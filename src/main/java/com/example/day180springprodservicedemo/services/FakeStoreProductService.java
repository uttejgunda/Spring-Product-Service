package com.example.day180springprodservicedemo.services;

import com.example.day180springprodservicedemo.dto.FakeStoreResponse;
import com.example.day180springprodservicedemo.models.Category;
import com.example.day180springprodservicedemo.models.Product;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class FakeStoreProductService implements ProductService {

    RestTemplate restTemplate = new RestTemplate();
    @Override
    public Product getSingleProduct(String productId) {
        FakeStoreResponse response = restTemplate.getForObject("https://fakestoreapi.com/products/"+productId, FakeStoreResponse.class );

        Product product = new Product();
        product.setId(response.getId());
        product.setName(response.getTitle());
        product.setPrice(response.getPrice() * 1.0);
        product.setDescription(response.getDescription());
        product.setImageUrl(response.getImage());
        Category category = new Category();
        category.setName(response.getCategory());
        product.setCategory(category);
        return product;
    }

    @Override
    public List<Product> getAllProducts() {
        FakeStoreResponse[] responseArray = restTemplate.getForObject("https://fakestoreapi.com/products/", FakeStoreResponse[].class );


        List<Product> productsList = new ArrayList<>();

        for(FakeStoreResponse response: responseArray) {
            Product product = new Product();
            product.setId(response.getId());
            product.setName(response.getTitle());
            product.setPrice(response.getPrice() * 1.0);
            product.setDescription(response.getDescription());
            product.setImageUrl(response.getImage());
            Category category = new Category();
            category.setName(response.getCategory());
            product.setCategory(category);

            productsList.add(product);
        }

        return productsList;
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
