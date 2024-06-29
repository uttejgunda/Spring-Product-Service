package com.example.day180springprodservicedemo.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter // CREATES GETTERS FOR ALL THE ATTRIBUTES OF THE CLASS
@Setter // CREATES SETTERS FOR ALL THE ATTRIBUTES OF THE CLASS
@NoArgsConstructor // CREATES A NO ARGUMENTS CONSTRUCTOR METHOD FOR THE CLASS
@AllArgsConstructor // CREATES THE CONSTRUCTOR METHOD FOR THE CLASS WITH ALL ATTRIBUTES AS ARGS IN THE SAME SEQUENCE
public class Product {
    private String id;
    private String name;
    private String description;
    @Setter Double price;  // IF WE WANT TO HAVE SETTER ONLY FOR SPECEFIC ATTRIBUTES, WE CAN ANNOTATE THIS WAY
    private String imageUrl;
    private Category category;

    public Product (String name) {
        this.name = name;
    }

    public void changePrice(double newPrice) {
        this.price = newPrice;
    }

}
