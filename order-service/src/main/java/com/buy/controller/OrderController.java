package com.buy.controller;

import com.buy.dtos.OrderDTO;
import com.buy.dtos.OrderResponse;
import com.buy.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/orders")
public class OrderController {

    private final OrderService orderService;
    private final ModelMapper orderMapper;

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping(path = "{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable("orderId") Integer id) {
        OrderResponse order = orderService.getOrder(id);
        return ResponseEntity.ok(order);
    }
    @PostMapping
     public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        OrderDTO createdOrder = orderService.saveOrder(orderDTO);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }
    @PutMapping(path = "{orderId}")
    public ResponseEntity<String> updateProduct(@PathVariable("orderId") Integer id,
                                                @Valid @RequestBody OrderDTO orderDTO) {
        orderService.updateOrder(id, orderDTO);
        return new ResponseEntity<>("All changes are done", HttpStatus.OK);
    }
    @DeleteMapping(path = "{orderId}")
    public ResponseEntity<String> deleteProduct(@PathVariable("orderId") Integer id) {
        orderService.deleteOrder(id);
        return new ResponseEntity<>("Product has been deleted successfully!!", HttpStatus.OK);
    }
}
