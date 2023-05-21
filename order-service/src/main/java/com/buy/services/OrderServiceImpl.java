package com.buy.services;

import com.buy.config.ProductServiceClient;
import com.buy.dto.OrderDTO;
import com.buy.dto.OrderResponse;
import com.buy.dto.ProductDTO;
import com.buy.exceptions.OrderNotFoundException;
import com.buy.model.Order;
import com.buy.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper orderMapper;
    private final ProductServiceClient productServiceClient;

    @Override
    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(prd -> getOrderResponse(orderMapper.map(prd, OrderDTO.class)))
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse getOrder(Integer orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isEmpty()) {
            throw new OrderNotFoundException("Order not found for id " + orderId);
        }
        return getOrderResponse(orderMapper.map(order, OrderDTO.class));
    }

    @Override
    public void updateOrder(Integer orderId, OrderDTO orderDTO) {
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        if (orderDTO.getProductId() != null) {
            order.setProductId(orderDTO.getProductId());
        }
        if (orderDTO.getQuantity() != 0) {
            order.setQuantity(orderDTO.getQuantity());
        }
        if (orderDTO.getCustomerName() != null && !orderDTO.getCustomerName().isEmpty()) {
            order.setCustomerName(orderDTO.getCustomerName());
        }
        orderRepository.save(order);
    }

    @Override
    public OrderDTO saveOrder(OrderDTO orderDTO) {

        List<Order> orders = orderRepository.findAll().stream().toList();
        Order order = orderMapper.map(orderDTO, Order.class);
        boolean check = orders.contains(order);
        if (check) {
            throw new OrderNotFoundException("Order with Id " + orderDTO.getId() + " already exists");
        }
        System.out.println(getProductDetails(orderDTO));
        order.setProductId(getProductDetails(orderDTO).getId());

        orderRepository.save(order);
        return orderMapper.map(order, OrderDTO.class);
    }

    @Override
    public void deleteOrder(Integer orderId) {
        Optional<Order> order = orderRepository.findById(orderId);

        if (order.isEmpty()) {
            throw new OrderNotFoundException("Order not found for id " + orderId);
        }
        orderRepository.delete(orderMapper.map(order, Order.class));
    }
    public OrderResponse getOrderResponse(OrderDTO orderDTO) {
        OrderResponse newOrderResponse = new OrderResponse();
        newOrderResponse.setId(orderDTO.getId());
        newOrderResponse.setProductName(getProductDetails(orderDTO).getName());
        newOrderResponse.setQuantity(orderDTO.getQuantity());
        newOrderResponse.setCustomerName(orderDTO.getCustomerName());
        return newOrderResponse;
    }

    private ProductDTO getProductDetails(OrderDTO orderDTO) {
        ProductDTO receivedProduct = productServiceClient.getProduct(orderDTO.getProductId()).getBody();
        return receivedProduct;
    }

}
