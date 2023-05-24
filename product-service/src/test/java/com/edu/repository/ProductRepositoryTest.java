package com.edu.repository;

import com.edu.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class ProductRepositoryTest {

    @Mock
    private ProductRepository productRepository;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testIfShouldFindByName() {
        String productName = "Nokia";

        Product product = new Product(1, productName, "model 3310", 200.0, 3);
        when(productRepository.findByName(productName)).thenReturn(product);
        Product foundProduct = productRepository.findByName(productName);

        assertThat(foundProduct).isNotNull();
        assertThat(foundProduct.getName()).isEqualTo(productName);
    }
}