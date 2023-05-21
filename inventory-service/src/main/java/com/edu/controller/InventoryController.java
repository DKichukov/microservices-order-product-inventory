package com.edu.controller;

import com.edu.dtos.InventoryResponse;
import com.edu.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {
    private final InventoryService inventoryService;
    @GetMapping
    public ResponseEntity<List<InventoryResponse>> getAllInventories(){
        return ResponseEntity.ok(inventoryService.getAllProducts());
    }
}
