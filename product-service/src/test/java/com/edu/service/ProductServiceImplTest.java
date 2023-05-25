package com.edu.service;

import com.edu.dto.ProductDTO;
import com.edu.exceptions.ApiRequestException;
import com.edu.model.Product;
import com.edu.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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
    @InjectMocks
    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        productServiceImpl = new ProductServiceImpl(productRepository, modelMapper);
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
    void testUpdateProduct_QuantityIsZero_ShouldDeleteProduct() {
        Integer productId = 1;
        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setQuantity(0);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setQuantity(0);

        when(productRepository.findById(productId)).thenReturn(java.util.Optional.of(existingProduct));

        productServiceImpl.updateProduct(productId, productDTO);

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).delete(existingProduct);
        verifyNoMoreInteractions(productRepository);
    }
    @Test
    public void testGetProductWithNonExistingIdShouldThrowApiRequestException() {
        Integer productId = 1;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> productServiceImpl.getProduct(productId));

        verify(productRepository, times(1)).findById(productId);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    public void testSaveNewProduct() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Nokia");


        Product product = modelMapper.map(productDTO, Product.class);
        Product savedProduct = new Product();
        savedProduct.setId(1);
        savedProduct.setName("Nokia");

        when(productRepository.findByName(productDTO.getName())).thenReturn(null);

        when(productRepository.save(product)).thenReturn(savedProduct);

        ProductDTO result = productServiceImpl.saveProduct(productDTO);

        verify(productRepository, times(1)).findByName(productDTO.getName());
        verify(productRepository, times(1)).save(product);
        verifyNoMoreInteractions(productRepository);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(savedProduct.getId());
        assertThat(result.getName()).isEqualTo(savedProduct.getName());
    }

    @Test
    public void testSaveExistingProductShouldThrowApiRequestException() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Nokia");

        Product existingProduct = new Product();
        existingProduct.setId(1);
        existingProduct.setName("Nokia");

        when(productRepository.findByName(productDTO.getName())).thenReturn(existingProduct);

        assertThrows(ApiRequestException.class, () -> productServiceImpl.saveProduct(productDTO));

        verify(productRepository, times(1)).findByName(productDTO.getName());
        verifyNoMoreInteractions(productRepository);
    }
    @Test
    void testExistingProductShouldUpdate() {

        Integer productId = 1;
        ProductDTO productDTO = modelMapper.map(product1, ProductDTO.class);

        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setName("Nokia 3310");
        existingProduct.setPrice(49.99);
        existingProduct.setDescription("Microsoft phone");
        existingProduct.setQuantity(5);

        when(productRepository.findById(productId)).thenReturn(java.util.Optional.of(existingProduct));

        productServiceImpl.updateProduct(productId, productDTO);

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(existingProduct);

        assertThat(existingProduct.getName()).isEqualTo(productDTO.getName());
        assertThat(existingProduct.getPrice()).isEqualTo(productDTO.getPrice());
        assertThat(existingProduct.getDescription()).isEqualTo(productDTO.getDescription());
        assertThat(existingProduct.getQuantity()).isEqualTo(productDTO.getQuantity());
    }
    @Test
    void testNonExistingProductShouldThrowException() {
        Integer productId = 1;
        ProductDTO productDTO = modelMapper.map(product1, ProductDTO.class);

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> productServiceImpl.updateProduct(productId, productDTO));

        verify(productRepository, times(1)).findById(productId);
        verifyNoMoreInteractions(productRepository);
    }
    @Test
    void testDeleteExistingProductShouldBeDelete() {

        Integer productId = 1;
        Product existingProduct = new Product();
        existingProduct.setId(productId);

        when(productRepository.findById(productId)).thenReturn(java.util.Optional.of(existingProduct));
        productServiceImpl.delete(productId);

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).delete(existingProduct);
    }
    @Test
    void testDeleteNonExistingProductShouldThrowException() {
        Integer productId = 1;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());
        assertThrows(ApiRequestException.class, () -> productServiceImpl.delete(productId));

        verify(productRepository, times(1)).findById(productId);
        verifyNoMoreInteractions(productRepository);
    }

}
