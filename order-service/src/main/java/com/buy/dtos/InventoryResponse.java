package com.buy.dtos;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class InventoryResponse {
    private Integer id;
    private Integer quantity;

}
