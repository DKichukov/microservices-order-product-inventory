package com.edu.controller;

import com.edu.dtos.InventoryResponse;
import com.edu.exceptions.ApiRequestException;
import com.edu.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(InventoryControllerTest.class)
class InventoryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private InventoryService inventoryService;

    @Test
    public void testGetProductsShouldFetchAllInventories() throws Exception {

        given(inventoryService.getAllProducts()).willReturn(List.of(
                InventoryResponse.builder().id(1).quantity(5).build(),
                InventoryResponse.builder().id(2).quantity(10).build(),
                InventoryResponse.builder().id(3).quantity(15).build()
        ));
        mockMvc.perform(get("/api/v1/inventories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].quantity", is(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].quantity", is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].id", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].quantity", is(15)));

        verify(inventoryService, times(1)).getAllProducts();
        verifyNoMoreInteractions(inventoryService);
    }
    @Test
    public void testShouldGetDesiredInventory() throws Exception {
        given(inventoryService.getProductById(anyInt())).willReturn(
                InventoryResponse.builder().id(1).quantity(5).build()
        );

        mockMvc.perform(get("/api/v1/inventories/{productId}", 1))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("name").value(5));
    }
    @Test
    void testGetInventoryForMissingInventoryShouldThrowException404() throws Exception {

        given(inventoryService.getProductById(anyInt())).willThrow(ApiRequestException.class);

        mockMvc.perform(get("/api/v1/inventories/1"))
                .andExpect(status().isNotFound());

    }

}