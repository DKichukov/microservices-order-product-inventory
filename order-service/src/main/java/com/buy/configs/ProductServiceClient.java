package com.buy.configs;

import com.buy.dtos.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "product-service",url = "http://localhost:8083/api/v1/products")
public interface ProductServiceClient
{

    @GetMapping
    List<ProductDTO> getAllProducts();
    @GetMapping(path = "{productId}")
    ResponseEntity<ProductDTO> getProduct(@PathVariable("productId") Integer productId);
    @PutMapping(path = "{productId}")
    ResponseEntity<String> updateProduct(@PathVariable("productId") Integer id,
                                                @Valid @RequestBody ProductDTO productDTO);

}
