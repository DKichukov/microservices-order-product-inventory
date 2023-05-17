package com.buy.controller;

import com.buy.dto.ProductDTO;
import com.buy.config.ProductProxyServer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductProxyServer productProxyServer;

    @GetMapping("/")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productProxyServer.getAllProducts());
    }

    @GetMapping(path = "{productId}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable("productId") Integer id) {
        ProductDTO receivedProduct = productProxyServer.getProduct(id).getBody();
        return ResponseEntity.ok(receivedProduct);
    }
}
