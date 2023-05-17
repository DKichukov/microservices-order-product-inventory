package com.buy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Integer id;
    @Min(1)
    private Integer productId;
    @Min(1)
    private Integer quantity;
    @Column(length = 64, nullable = false)
    private String customerName;
}
