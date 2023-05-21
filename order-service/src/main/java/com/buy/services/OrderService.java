package com.buy.services;

import com.buy.dtos.OrderDTO;
import com.buy.dtos.OrderResponse;

import java.util.List;

public interface OrderService {

    List<OrderResponse> getAllOrders();

    OrderResponse getOrder(Integer orderId);

    OrderDTO createOrder(OrderDTO orderDTO);

    void updateOrder(Integer orderId, OrderDTO orderDTO);

    void deleteOrder(Integer orderId);
}
