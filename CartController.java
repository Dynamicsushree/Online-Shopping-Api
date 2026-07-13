package com.example.onlineshop.controller;

import com.example.onlineshop.dto.CartItemRequest;
import com.example.onlineshop.entity.CartItem;
import com.example.onlineshop.entity.User;
import com.example.onlineshop.repository.UserRepository;
import com.example.onlineshop.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;
    private final UserRepository userRepository;

    public CartController(CartService cartService, UserRepository userRepository) {
        this.cartService = cartService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        return ResponseEntity.ok(cartService.getCartItems(user));
    }

    @PostMapping
    public ResponseEntity<CartItem> addCartItem(@AuthenticationPrincipal UserDetails userDetails,
                                                @RequestBody CartItemRequest request) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        return ResponseEntity.ok(cartService.addOrUpdateCartItem(user, request));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> removeCartItem(@AuthenticationPrincipal UserDetails userDetails,
                                               @PathVariable Long itemId) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        cartService.removeCartItem(user, itemId);
        return ResponseEntity.noContent().build();
    }
}
