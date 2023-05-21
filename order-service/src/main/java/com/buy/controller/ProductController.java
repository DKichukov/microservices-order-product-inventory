package com.buy.controller;

import com.buy.configs.ProductServiceClient;
import com.buy.dtos.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductServiceClient productServiceClient;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productServiceClient.getAllProducts());
    }

    @GetMapping(path = "{productId}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable("productId") Integer id) {
        ProductDTO receivedProduct = productServiceClient.getProduct(id).getBody();
        return ResponseEntity.ok(receivedProduct);
    }
}
