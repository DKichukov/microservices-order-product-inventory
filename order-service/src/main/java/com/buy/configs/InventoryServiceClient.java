package com.buy.configs;

import com.buy.dtos.InventoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "inventory-service",url = "http://localhost:8082/api/v1/inventories")
public interface InventoryServiceClient
{

    @GetMapping
    List<InventoryResponse> getAllInventories();
    @GetMapping(path = "{productId}")
    InventoryResponse getInventory(@PathVariable("productId") Integer productId);

}
