package com.edu.service;

import com.edu.dto.ProductDTO;
import com.edu.exceptions.ApiRequestException;
import com.edu.model.Product;
import com.edu.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    @Mock
    private ProductRepository productRepository;
    private ProductServiceImpl productServiceImpl;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        productServiceImpl = new ProductServiceImpl(productRepository, new ModelMapper());
    }

   final Product product1 = new Product(1, "Nokia", "model 3310", 200.0, 3);
   final Product product2 = new Product(2, "SONY", "model ERRICSON", 1200.0,3);
   final Product product3 = new Product(3, "IPHONE", "model 23", 1999.0,3);
    final List<Product> products = List.of(product1, product2, product3);


    @Test
    public void testShouldGetAllProducts() {

        when(productRepository.findAll()).thenReturn(products);

        List<ProductDTO> result = productServiceImpl.getAllProducts();

        assertThat(result).hasSize(3);
        assertThat(result).extracting(ProductDTO::getName)
                .containsExactly("Nokia", "SONY", "IPHONE");

        verify(productRepository, times(1)).findAll();
        verifyNoMoreInteractions(productRepository);
    }


    @Test
    public void testGetProductByIdShouldExists() {
        Integer productId = 1;
        when(productRepository.findById(productId)).thenReturn(Optional.of(product1));
        ProductDTO result = productServiceImpl.getProduct(productId);

        verify(productRepository, times(1)).findById(productId);
        verifyNoMoreInteractions(productRepository);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(productId);
        assertThat(result.getName()).isEqualTo("Nokia");
    }
    @Test
    public void testGetProductIfNonExistingIdShouldThrowApiRequestException() {
        Integer productId = 1;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> productServiceImpl.getProduct(productId));

        verify(productRepository, times(1)).findById(productId);
        verifyNoMoreInteractions(productRepository);
    }
    @Test
    public void testGetProductWithGivenIdShouldShouldExists() {

        Integer productId = 1;

        when(productRepository.findById(productId)).thenReturn(Optional.of(product1));

        ProductDTO result = productServiceImpl.getProduct(productId);

        verify(productRepository, times(1)).findById(productId);
        verifyNoMoreInteractions(productRepository);


        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(productId);
        assertThat(result.getName()).isEqualTo("Nokia");
    }
    @Test
    public void testGetProductWithNonExistingIdShouldThrowApiRequestException() {
        Integer productId = 1;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> productServiceImpl.getProduct(productId));

        verify(productRepository, times(1)).findById(productId);
        verifyNoMoreInteractions(productRepository);
    }

}
