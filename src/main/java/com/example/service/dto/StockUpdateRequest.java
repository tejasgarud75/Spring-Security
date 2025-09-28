package com.example.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockUpdateRequest {

    private Integer quantity;

    private String notes;

}