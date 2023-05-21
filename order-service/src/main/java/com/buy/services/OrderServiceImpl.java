package com.buy.services;

import com.buy.configs.InventoryServiceClient;
import com.buy.configs.ProductServiceClient;
import com.buy.dtos.OrderDTO;
import com.buy.dtos.OrderResponse;
import com.buy.dtos.ProductDTO;
import com.buy.exceptions.ApiRequestException;
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
    private final InventoryServiceClient inventoryServiceClient;

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
            throw new ApiRequestException("Order not found for id " + orderId);
        }
        return getOrderResponse(orderMapper.map(order, OrderDTO.class));
    }

    @Override
    public void updateOrder(Integer orderId, OrderDTO orderDTO) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ApiRequestException("Order not found"));
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
    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = orderMapper.map(orderDTO, Order.class);
        ProductDTO productDTO = orderMapper.map(inventoryServiceClient.getInventory(orderDTO.getProductId()), ProductDTO.class);

        boolean orderExists = orderRepository.findAll().stream().anyMatch(each -> each.getId().equals(order.getId()));
        if (orderExists) {
            throw new ApiRequestException("Order with Id " + orderDTO.getId() + " already exists");
        } else if (productDTO != null && productDTO.getQuantity() > 0 && (productDTO.getQuantity() >= order.getQuantity())) {
            updateProductInfoInOrder(orderDTO, order, productDTO);
            updateProductQuantity(order, productDTO);
        } else {
            throw new ApiRequestException("Not enough quantity to place order");
        }
        return orderMapper.map(order, OrderDTO.class);
    }

    private void updateProductInfoInOrder(OrderDTO orderDTO, Order order, ProductDTO productDTO) {
        order.setProductId(productDTO.getId());
        order.setQuantity(orderDTO.getQuantity());
        order.setCustomerName(orderDTO.getCustomerName());
        orderRepository.save(order);
    }

    private void updateProductQuantity(Order order, ProductDTO productDTO) {
        ProductDTO editedProduct = orderMapper.map(inventoryServiceClient.getInventory(productDTO.getId()), ProductDTO.class);
        editedProduct.setQuantity(productDTO.getQuantity() - order.getQuantity());
        productServiceClient.updateProduct(editedProduct.getId(), editedProduct);
    }

    @Override
    public void deleteOrder(Integer orderId) {
        Optional<Order> order = orderRepository.findById(orderId);

        if (order.isEmpty()) {
            throw new ApiRequestException("Order not found for id " + orderId);
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
        return productServiceClient.getProduct(orderDTO.getProductId()).getBody();
    }


}
