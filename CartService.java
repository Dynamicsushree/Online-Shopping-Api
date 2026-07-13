package com.example.onlineshop.service;

import com.example.onlineshop.dto.CartItemRequest;
import com.example.onlineshop.entity.CartItem;
import com.example.onlineshop.entity.Product;
import com.example.onlineshop.entity.User;
import com.example.onlineshop.repository.CartItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final ProductService productService;

    public CartService(CartItemRepository cartItemRepository, ProductService productService) {
        this.cartItemRepository = cartItemRepository;
        this.productService = productService;
    }

    public List<CartItem> getCartItems(User user) {
        return cartItemRepository.findByUser(user);
    }

    @Transactional
    public CartItem addOrUpdateCartItem(User user, CartItemRequest request) {
        Product product = productService.findById(request.getProductId());
        if (request.getQuantity() <= 0 || request.getQuantity() > product.getStock()) {
            throw new IllegalArgumentException("Invalid quantity");
        }

        return cartItemRepository.findByUser(user).stream()
                .filter(item -> item.getProduct().getId().equals(request.getProductId()))
                .findFirst()
                .map(item -> {
                    item.setQuantity(request.getQuantity());
                    return cartItemRepository.save(item);
                })
                .orElseGet(() -> cartItemRepository.save(new CartItem(user, product, request.getQuantity())));
    }

    public void removeCartItem(User user, Long itemId) {
        CartItem cartItem = cartItemRepository.findByIdAndUser(itemId, user)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));
        cartItemRepository.delete(cartItem);
    }

    @Transactional
    public void clearCart(User user) {
        cartItemRepository.deleteByUser(user);
    }
}
