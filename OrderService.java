package com.example.onlineshop.service;

import com.example.onlineshop.dto.OrderRequest;
import com.example.onlineshop.entity.CartItem;
import com.example.onlineshop.entity.OrderEntity;
import com.example.onlineshop.entity.Payment;
import com.example.onlineshop.entity.User;
import com.example.onlineshop.repository.OrderRepository;
import com.example.onlineshop.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final CartService cartService;

    public OrderService(OrderRepository orderRepository, PaymentRepository paymentRepository, CartService cartService) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.cartService = cartService;
    }

    public List<OrderEntity> getOrders(User user) {
        return orderRepository.findByUser(user);
    }

    public OrderEntity getOrder(User user, Long orderId) {
        return orderRepository.findByIdAndUser(orderId, user)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }

    @Transactional
    public OrderEntity createOrder(User user, OrderRequest request) {
        List<CartItem> cartItems = cartService.getCartItems(user);
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        double total = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        OrderEntity order = new OrderEntity(user, total, "PENDING", LocalDateTime.now());
        order = orderRepository.save(order);

        Payment payment = new Payment(order, request.getPaymentMethod(), total, "COMPLETED", LocalDateTime.now());
        paymentRepository.save(payment);

        cartService.clearCart(user);
        order.setStatus("COMPLETED");
        return orderRepository.save(order);
    }
}
