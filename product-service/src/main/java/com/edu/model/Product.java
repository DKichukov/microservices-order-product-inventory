package com.edu.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;

@Data
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 64, nullable = false)
    private String name;
    @Column(length = 500)
    private String description;
    @PositiveOrZero
    private double price;
    @PositiveOrZero
    private int quantity;
}
