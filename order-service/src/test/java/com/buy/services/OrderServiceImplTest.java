package com.buy.services;

import com.buy.configs.InventoryServiceClient;
import com.buy.configs.ProductServiceClient;
import com.buy.dtos.OrderDTO;
import com.buy.dtos.OrderResponse;
import com.buy.dtos.ProductDTO;
import com.buy.exceptions.ApiRequestException;
import com.buy.model.Order;
import com.buy.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private OrderServiceImpl orderServiceImpl;
    @Mock
    private ProductServiceClient productServiceClient;
    @Mock
    private InventoryServiceClient inventoryServiceClient;
    @Mock
    private ModelMapper orderMapper;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testShouldGetAllOrders() {

        List<Order> orders = List.of(
                Order.builder().id(1).productId(100).quantity(5).customerName("John").build(),
                Order.builder().id(2).productId(200).quantity(10).customerName("Jane").build());

        Mockito.when(orderRepository.findAll()).thenReturn(orders);

        Mockito.when(productServiceClient.getProduct(100)).thenReturn(ResponseEntity.ok(ProductDTO.builder().id(100).name("Product 1").build()));
        Mockito.when(productServiceClient.getProduct(200)).thenReturn(ResponseEntity.ok(ProductDTO.builder().id(200).name("Product 2").build()));


        List<OrderResponse> orderResponses = orderServiceImpl.getAllOrders();

        assertEquals(2, orderResponses.size());

        OrderResponse orderResponse1 = orderResponses.get(0);
        assertEquals(1, orderResponse1.getId());
        assertEquals("Product 1", orderResponse1.getProductName());
        assertEquals(5, orderResponse1.getQuantity());
        assertEquals("John", orderResponse1.getCustomerName());

        OrderResponse orderResponse2 = orderResponses.get(1);
        assertEquals(2, orderResponse2.getId());
        assertEquals("Product 2", orderResponse2.getProductName());
        assertEquals(10, orderResponse2.getQuantity());
        assertEquals("Jane", orderResponse2.getCustomerName());
    }

    @Test
    public void testGetOrder() {
        // Arrange
        int orderId = 123;
        Order order = new Order();
        OrderDTO orderDTO = new OrderDTO();
        OrderResponse expectedResponse = new OrderResponse();

        when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.of(order));
        when(orderMapper.map(order, OrderDTO.class)).thenReturn(orderDTO);
        when(orderMapper.map(orderDTO, OrderResponse.class)).thenReturn(expectedResponse);

        OrderResponse actualResponse = orderServiceImpl.getOrder(orderId);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testGetOrderIfNonExistingIdShouldThrowApiRequestException() {
        Integer productId = 1;
        when(orderRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> orderServiceImpl.getOrder(productId));

        verify(orderRepository, times(1)).findById(productId);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    public void testExistsOrderShouldUpdate() {
        int orderId = 123;
        OrderDTO orderDTO = OrderDTO.builder().id(456).quantity(2).customerName("John Doe").build();
        Order existingOrder = Order.builder().id(orderId).build();

        when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.of(existingOrder));
        orderServiceImpl.updateOrder(orderId, orderDTO);

        assertThat(existingOrder.getProductId()).isEqualTo(orderDTO.getProductId());
        assertThat(existingOrder.getQuantity()).isEqualTo(orderDTO.getQuantity());
        assertThat(existingOrder.getCustomerName()).isEqualTo(orderDTO.getCustomerName());

        verify(orderRepository).save(existingOrder);
    }
    @Test
    void testNonExistingOrderShouldThrowException() {
        Integer orderId = 123;
        OrderDTO orderDTO = OrderDTO.builder().id(456).quantity(2).customerName("JohnDoe").build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());
        assertThrows(ApiRequestException.class, () -> orderServiceImpl.updateOrder(orderId, orderDTO));

        verify(orderRepository, times(1)).findById(orderId);
        verifyNoMoreInteractions(orderRepository);
    }


    @Test
    public void testDeleteNonExistingOrderShouldThrowException() {
        Integer orderId = 1;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());
        assertThrows(ApiRequestException.class, () -> orderServiceImpl.deleteOrder(orderId));

        verify(orderRepository, times(1)).findById(orderId);
        verifyNoMoreInteractions(orderRepository);
    }


}