package com.buy.services;

import com.buy.dto.OrderDTO;
import com.buy.dto.OrderResponse;

import java.util.List;

public interface OrderService {

    List<OrderResponse> getAllOrders();

    OrderResponse getOrder(Integer orderId);

    OrderDTO saveOrder(OrderDTO orderDTO);

    void updateOrder(Integer orderId, OrderDTO orderDTO);

    void deleteOrder(Integer orderId);
}
