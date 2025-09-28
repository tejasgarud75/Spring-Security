package com.example.service.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private Integer stockQuantity;
    private Integer lowStockThreshold;

}