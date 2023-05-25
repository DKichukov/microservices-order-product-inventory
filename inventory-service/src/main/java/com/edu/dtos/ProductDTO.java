package com.edu.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Integer id;
    @Column(length = 64, nullable = false)
    private String name;
    @Column(length = 500)
    private String description;
    private double price;
    private int quantity;
}
