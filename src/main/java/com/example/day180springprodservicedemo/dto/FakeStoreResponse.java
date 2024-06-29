package com.example.day180springprodservicedemo.dto;

import lombok.Getter;
import lombok.Setter;



// DTO is used to store data from outside world. So this DTO class will store all the data recd from JSON object

@Getter
@Setter
public class FakeStoreResponse {
    private String id;
    private String title;
    private Integer price;
    private String description;
    private String category;
    private String image;

}

