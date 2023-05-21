package com.buy.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ProductDTO {
    private Integer id;
    private String name;
    private String description;
    private double price;
    private int quantity;
}
