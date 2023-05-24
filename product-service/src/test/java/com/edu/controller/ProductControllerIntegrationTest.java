package com.edu.controller;

import com.edu.dto.ProductDTO;
import com.edu.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
class ProductControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetProductsShouldFetchAllProducts() throws Exception {
        ProductDTO product1 = new ProductDTO(1, "Nokia", "model 3310", 200.0, 3);
        ProductDTO product2 = new ProductDTO(2, "Samsung", "Galaxy S20", 999.99, 5);
        List<ProductDTO> productList = Arrays.asList(product1, product2);

        when(productService.getAllProducts()).thenReturn(productList);

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", is("Nokia")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description", is("model 3310")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].price", is(200.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].quantity", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", is("Samsung")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description", is("Galaxy S20")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].price", is(999.99)))
                .andExpect(jsonPath("$[1].quantity", is(5)));

        verify(productService, times(1)).getAllProducts();
        verifyNoMoreInteractions(productService);
    }

    @Test
    public void testShouldGetDesiredProduct() throws Exception {
        given(productService.getProduct(anyInt())).willReturn(
                ProductDTO.builder()
                        .id(1)
                        .name("Nokia")
                        .description("3310")
                        .price(100)
                        .quantity(1).build()
        );
        mockMvc.perform(get("/api/v1/products/{productId}", 1))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("name").value("Nokia"))
                .andExpect(MockMvcResultMatchers.jsonPath("description").value("3310"))
                .andExpect(MockMvcResultMatchers.jsonPath("price").value(100))
                .andExpect(MockMvcResultMatchers.jsonPath("quantity").value(1));
    }

    @Test
    public void testShouldCreateProduct() throws Exception {
        ProductDTO productDTO = ProductDTO.builder()
                .id(1)
                .name("Nokia")
                .description("3310")
                .price(100)
                .quantity(1).build();

        given(productService.saveProduct(any(ProductDTO.class))).willReturn(productDTO);
        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isCreated())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("name").value("Nokia"))
                .andExpect(MockMvcResultMatchers.jsonPath("description").value("3310"))
                .andExpect(MockMvcResultMatchers.jsonPath("price").value(100))
                .andExpect(MockMvcResultMatchers.jsonPath("quantity").value(1));
    }
    @Test
    public void testShouldUpdateProduct() throws Exception {
        ProductDTO productDTO = ProductDTO.builder()
                .name("Updated Nokia")
                .description("Updated 3310")
                .price(200)
                .quantity(2)
                .build();

        mockMvc.perform(put("/api/v1/products/{productId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("All changes are done"));
        verify(productService, times(1)).updateProduct(eq(1), eq(productDTO));
    }
    @Test
    public void testShouldDeleteProduct() throws Exception {
        mockMvc.perform(delete("/api/v1/products/{productId}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string("Product has been deleted successfully!!"));
        verify(productService, times(1)).delete(eq(1));
    }

}
