package com.buy.dtos;

import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.Min;

@Getter
@Setter
@Builder
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
