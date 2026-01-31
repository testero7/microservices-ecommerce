package com.ecommerce.orderservice.controller;

import com.ecommerce.orderservice.dto.AddressDTO;
import com.ecommerce.orderservice.model.Order;
import com.ecommerce.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(
            @RequestParam Integer userId,
            @RequestBody AddressDTO addressDTO) {
        Order order = orderService.createOrder(userId, addressDTO);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Integer orderId) {
        Order order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getAllOrdersByUser(@PathVariable Integer userId) {
        List<Order> orders = orderService.getAllOrdersByUser(userId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable Integer orderId,
            @RequestBody Map<String, String> request) {
        String status = request.get("orderStatus");
        Order order = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(order);
    }

    @DeleteMapping("/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable Integer orderId) {
        String message = orderService.cancelOrder(orderId);
        return ResponseEntity.ok(message);
    }
}