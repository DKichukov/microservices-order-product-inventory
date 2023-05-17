package com.edu.service;

import com.edu.dto.ProductDTO;

import java.util.List;

public interface ProductService {
    List<ProductDTO> getAllProducts();

    ProductDTO getProduct(Integer productId);

    ProductDTO saveProduct(ProductDTO productDTO);

    void updateProduct(Integer productId, ProductDTO productDTO);

    void delete(Integer productId);
}
