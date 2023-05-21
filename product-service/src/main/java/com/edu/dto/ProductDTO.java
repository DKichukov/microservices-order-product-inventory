package com.edu.dto;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "products")
public class ProductDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 64, nullable = false)
    private String name;
    @Column(length = 500)
    private String description;
    private double price;
    private int quantity;
}
