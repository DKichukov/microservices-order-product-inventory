package com.buy.controller;

import com.buy.dtos.OrderDTO;
import com.buy.dtos.OrderResponse;
import com.buy.services.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(OrderController.class)
class OrderControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ModelMapper mapper;
    @MockBean
    private OrderService orderService;

    @Test
    public void testGetProductsShouldFetchAllOrders() throws Exception {
        given(orderService.getAllOrders()).willReturn(List.of(OrderResponse.builder().id(1).customerName("ivan").build(),
                OrderResponse.builder().id(2).customerName("Maria").build()));

        mockMvc.perform(get("/api/v1/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].customerName", is("ivan")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].customerName", is("Maria")));
        verify(orderService, times(1)).getAllOrders();
        verifyNoMoreInteractions(orderService);

    }

    @Test
    public void testShouldGetDesiredOrder() throws Exception {
        given(orderService.getOrder(anyInt())).willReturn(OrderResponse.builder().id(1).customerName("ivan").build());
        mockMvc.perform(get("/api/v1/orders/{productId}", 1))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("customerName").value("ivan"));
        verify(orderService, times(1)).getOrder(1);
        verifyNoMoreInteractions(orderService);
    }
    @Test
    public void testShouldCreateOrder() throws Exception {

        OrderDTO orderDTO = OrderDTO.builder().id(1).customerName(
                "ivan").build();
        given(orderService.createOrder(any(OrderDTO.class))).willReturn(orderDTO);
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isCreated())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("customerName").value("ivan"));
    }
    @Test
    public void testShouldUpdateOrder() throws Exception {
        OrderDTO orderDTO = OrderDTO.builder().id(1).customerName(
                "ivan").build();
        mockMvc.perform(put("/api/v1/orders/{orderId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("All changes are done"));
        verify(orderService, times(1)).updateOrder(eq(1), eq(orderDTO));
    }
    @Test
    public void testShouldDeleteOrder() throws Exception {
        mockMvc.perform(delete("/api/v1/orders/{orderId}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string("Product has been deleted successfully!!"));
        verify(orderService, times(1)).deleteOrder(eq(1));
    }


}