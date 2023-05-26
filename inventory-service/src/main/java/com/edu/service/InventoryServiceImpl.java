package com.edu.service;

import com.edu.configs.ProductServiceClient;
import com.edu.dtos.InventoryResponse;
import com.edu.dtos.ProductDTO;
import com.edu.exceptions.ApiRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final ProductServiceClient productServiceClient;

    @Override
    public List<InventoryResponse> getAllProducts() {
        return getProductToInventoryResponse(productServiceClient.getAllProducts()
                .stream().toList());
    }

    @Override
    public InventoryResponse getProductById(Integer productId) {
        return getProductToInventoryResponse(productServiceClient.getAllProducts().stream().toList())
                .stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ApiRequestException("Product with id: " + productId + " not found"));
    }

    List<InventoryResponse> getProductToInventoryResponse(List<ProductDTO> productDTOList) {
        return productDTOList.stream().map(
                product -> {
                    InventoryResponse response = new InventoryResponse();
                    response.setId(product.getId());
                    response.setQuantity(product.getQuantity());
                    return response;
                }
        ).toList();
    }
}
