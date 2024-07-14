package com.example.day180springprodservicedemo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
// This class itself is not going to a table. This is just having the extra attributes to be inherited to required classes
@MappedSuperclass
public class BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // We are making this long so that we will have a number that keeps auto-incrementing.
    private Date createAt;
    private Date updatedAt;
    private Boolean isDeleted;
}
