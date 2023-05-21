package com.edu.service;

import com.edu.dtos.InventoryResponse;

import java.util.List;

public interface InventoryService {
    List<InventoryResponse> getAllProducts();
}
