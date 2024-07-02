package com.example.day180springprodservicedemo.dto;


// {
//         title: 'test product',
//         price: 13.5,
//         description: 'lorem ipsum set',
//         image: 'https://i.pravatar.cc',
//         category: 'electronic'
//         }

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
public class FakeStoreRequestDto {

    private String title;
    private Double price;
    private String description;
    private String image;
    private String Category;
}
