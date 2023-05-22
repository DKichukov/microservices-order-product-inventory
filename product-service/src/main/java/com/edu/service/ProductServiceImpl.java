package com.edu.service;

import com.edu.dto.ProductDTO;
import com.edu.exceptions.ApiRequestException;
import com.edu.model.Product;
import com.edu.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper prodMapper;

    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products
                .stream()
                .map(prd -> prodMapper.map(prd, ProductDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO getProduct(Integer productId) {
        return prodMapper.map(
                productRepository.findById(productId)
                        .orElseThrow(() -> new ApiRequestException("Product not found for id " + productId)),
                ProductDTO.class);
    }

    @Override
    public ProductDTO saveProduct(ProductDTO productDTO) {
        Product existingProduct = productRepository.findByName(productDTO.getName());

        if (existingProduct != null) {
            throw new ApiRequestException("Product with name " + productDTO.getName() + " already exists");
        }
        Product product = prodMapper.map(productDTO, Product.class);

        return prodMapper.map(productRepository.save(product), ProductDTO.class);
    }

    @Override
    public void updateProduct(Integer productId, ProductDTO productDTO) {
        Product product = productRepository.findById(productId)
                .orElseThrow(()->new ApiRequestException("Product not found"));

        if (productDTO.getName() != null && !productDTO.getName().isEmpty()) {
            product.setName(productDTO.getName());
        }
        if (productDTO.getPrice() != 0) {
            product.setPrice(productDTO.getPrice());
        }
        if (productDTO.getDescription() != null && !productDTO.getDescription().isEmpty()) {
            product.setDescription(productDTO.getDescription());
        }
        if (productDTO.getQuantity() > 0) {
            product.setQuantity(productDTO.getQuantity());
        } else {
            productRepository.delete(product);
            return;
        }
        productRepository.save(product);
    }


    @Override
    public void delete(Integer productId) {
        Product product = productRepository.findById(productId).orElseThrow(()->new ApiRequestException("Product not found for id " + productId));

        productRepository.delete(prodMapper.map(product, Product.class));
    }
}
