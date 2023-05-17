package com.buy.config;

import com.buy.dto.ProductDTO;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "proxy",url = "http://localhost:8083/api/v1/products")
public interface ProductProxyServer {

    @GetMapping("/")
    List<ProductDTO> getAllProducts();
    @GetMapping(path = "{productId}")
    ResponseEntity<ProductDTO> getProduct(@PathVariable("productId") Integer productId);

}
