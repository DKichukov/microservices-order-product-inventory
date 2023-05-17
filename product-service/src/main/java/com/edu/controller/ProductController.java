package com.edu.controller;

import com.edu.dto.ProductDTO;
import com.edu.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/")
    public ResponseEntity<List<ProductDTO>> getProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping(path = "{productId}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable("productId") Integer productId) {
        ProductDTO productDTO = productService.getProduct(productId);
        return ResponseEntity.ok(productDTO);
    }

    @PostMapping("/")
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        ProductDTO createdProduct = productService.saveProduct(productDTO);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

        @PutMapping(path = "{productId}")
    public ResponseEntity<String> updateProduct(@PathVariable("productId") Integer id,
                                                @Valid @RequestBody ProductDTO productDTO) {
        productService.updateProduct(id,productDTO);
        return new ResponseEntity<>("All changes are done", HttpStatus.OK);
    }
    @DeleteMapping(path = "{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable("productId") Integer id) {
        productService.delete(id);
        return new ResponseEntity<>("Product has been deleted successfully!!", HttpStatus.OK);
    }


}
