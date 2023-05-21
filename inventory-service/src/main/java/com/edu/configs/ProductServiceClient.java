package com.edu.configs;

import com.edu.dtos.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
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
    ProductDTO getProduct(@PathVariable("productId") Integer productId);
    @PutMapping(path = "{productId}")
    String updateProduct(@PathVariable("productId") Integer id,
                                                @Valid @RequestBody ProductDTO productDTO);

}
