package com.example.service.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProductRequest {
    private Long id;
    private String name;
    private String description;
    private Integer stockQuantity;
    private Integer lowStockThreshold;

}