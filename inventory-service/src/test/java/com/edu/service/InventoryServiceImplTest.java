package com.edu.service;

import com.edu.configs.ProductServiceClient;
import com.edu.dtos.InventoryResponse;
import com.edu.dtos.ProductDTO;
import com.edu.exceptions.ApiRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class InventoryServiceImplTest {

    private InventoryServiceImpl inventoryService;
    @Mock
    private ProductServiceClient productServiceClient;
//    @InjectMocks
//    private ModelMapper responseMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        inventoryService = new InventoryServiceImpl(productServiceClient);
    }

    @Test
    public void testShouldGetAllInventories() {

        List<ProductDTO> productDTOList = List.of(
                ProductDTO.builder().id(1).name("Product 1").quantity(1).build(),
                ProductDTO.builder().id(2).name("Product 2").quantity(2).build(),
                ProductDTO.builder().id(3).name("Product 3").quantity(3).build()
        );
        given(productServiceClient.getAllProducts())
                .willReturn(productDTOList);

        List<InventoryResponse> expectedResponse = List.of(
                InventoryResponse.builder().id(1).quantity(1).build(),
                InventoryResponse.builder().id(2).quantity(2).build(),
                InventoryResponse.builder().id(3).quantity(3).build()
        );
        List<InventoryResponse> actualResponse = inventoryService.getAllProducts();

        assertEquals(expectedResponse, actualResponse);
        assertThat(expectedResponse).isEqualTo(actualResponse);
    }

    @Test
    public void testShouldGetProductById() {
        Integer productId = 2;
        List<ProductDTO> productDTOList = List.of(
                ProductDTO.builder().id(1).name("Product 1").quantity(1).build(),
                ProductDTO.builder().id(2).name("Product 2").quantity(2).build(),
                ProductDTO.builder().id(3).name("Product 3").quantity(3).build()
        );
        when(productServiceClient.getAllProducts()).thenReturn(productDTOList);

        InventoryResponse expectedResponse = InventoryResponse.builder()
                .id(productId)
                .quantity(2)
                .build();

        InventoryResponse actualResponse = inventoryService.getProductById(productId);

        assertEquals(expectedResponse, actualResponse);

        verify(productServiceClient).getAllProducts();
    }

    @Test
    public void testGetProductByIdShouldThrowsException() {
        Integer productId = 4;

        List<ProductDTO> productDTOList = List.of(
                ProductDTO.builder().id(1).name("Product 1").quantity(1).build(),
                ProductDTO.builder().id(2).name("Product 2").quantity(2).build(),
                ProductDTO.builder().id(3).name("Product 3").quantity(3).build()
        );

        given(productServiceClient.getAllProducts()).willReturn(productDTOList);

        assertThrows(ApiRequestException.class, () -> inventoryService.getProductById(productId));
    }

    @Test
    void getProductToInventoryResponse() {
        List<ProductDTO> productDTOList = List.of(
                ProductDTO.builder().id(1).name("Product 1").quantity(1).build(),
                ProductDTO.builder().id(2).name("Product 2").quantity(2).build(),
                ProductDTO.builder().id(3).name("Product 3").quantity(3).build()
        );
        List<InventoryResponse> response = inventoryService.getProductToInventoryResponse(productDTOList);

        List<InventoryResponse> expectedResponse = List.of(
                InventoryResponse.builder().id(1).quantity(1).build(),
                InventoryResponse.builder().id(2).quantity(2).build(),
                InventoryResponse.builder().id(3).quantity(3).build()
        );
        assertEquals(expectedResponse, response);
    }
}