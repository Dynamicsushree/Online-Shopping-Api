package com.example.onlineshop.controller;

import com.example.onlineshop.dto.OrderRequest;
import com.example.onlineshop.entity.OrderEntity;
import com.example.onlineshop.entity.User;
import com.example.onlineshop.repository.UserRepository;
import com.example.onlineshop.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final UserRepository userRepository;

    public OrderController(OrderService orderService, UserRepository userRepository) {
        this.orderService = orderService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<OrderEntity>> getOrders(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        return ResponseEntity.ok(orderService.getOrders(user));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderEntity> getOrder(@AuthenticationPrincipal UserDetails userDetails,
                                                @PathVariable Long orderId) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        return ResponseEntity.ok(orderService.getOrder(user, orderId));
    }

    @PostMapping
    public ResponseEntity<OrderEntity> createOrder(@AuthenticationPrincipal UserDetails userDetails,
                                                   @RequestBody OrderRequest request) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        return ResponseEntity.ok(orderService.createOrder(user, request));
    }
}
