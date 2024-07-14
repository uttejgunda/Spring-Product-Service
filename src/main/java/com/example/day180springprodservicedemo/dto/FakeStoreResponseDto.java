package com.example.day180springprodservicedemo.dto;

import com.example.day180springprodservicedemo.models.Category;
import com.example.day180springprodservicedemo.models.Product;
import lombok.Getter;
import lombok.Setter;



// DTO is used to store data from outside world. So this DTO class will store all the data recd from JSON object

@Getter
@Setter
public class FakeStoreResponseDto {
    private String id;
    private String title;
    private Integer price;
    private String description;
    private String category;
    private String image;

    public Product toProduct() {
        Product product = new Product();
        product.setId(Long.valueOf(this.getId()));
        product.setName(this.getTitle());
        product.setPrice(this.getPrice() * 1.0);
        product.setDescription(this.getDescription());
        product.setImageUrl(this.getImage());
        Category category = new Category();
        category.setName(this.getCategory());
//        product.setCategory(category);
        return product;
    }
}

