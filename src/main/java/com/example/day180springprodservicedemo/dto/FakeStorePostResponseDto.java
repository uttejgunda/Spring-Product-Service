package com.example.day180springprodservicedemo.dto;

import com.example.day180springprodservicedemo.models.Category;
import com.example.day180springprodservicedemo.models.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FakeStorePostResponseDto {
    private String id;
    private String title;
    private Integer price;
    private String description;
    private String category;
    private String image;

    public Product toProduct() {
        Product product = new Product();
        product.setId(this.getId());
        product.setName(this.getTitle());
        product.setPrice(this.getPrice() * 1.0);
        product.setDescription(this.getDescription());
        product.setImageUrl(this.getImage());
        Category category = new Category();
        category.setName(this.getCategory());
        product.setCategory(category);
        // ...
        // ...
        return product;
    }
}
