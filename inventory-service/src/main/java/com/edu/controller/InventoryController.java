package com.edu.controller;

import com.edu.dtos.InventoryResponse;
import com.edu.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/inventories")
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<List<InventoryResponse>> getAllInventories() {
        return ResponseEntity.ok(inventoryService.getAllProducts());
    }

    @GetMapping(path = "{productId}")
    ResponseEntity<InventoryResponse> getProductInventory(@PathVariable("productId") Integer productId) {
        return ResponseEntity.ok(inventoryService.getProductById(productId));
    }
}
