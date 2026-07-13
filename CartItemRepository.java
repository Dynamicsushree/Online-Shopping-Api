package com.example.onlineshop.repository;

import com.example.onlineshop.entity.CartItem;
import com.example.onlineshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(User user);
    Optional<CartItem> findByIdAndUser(Long id, User user);
    void deleteByUser(User user);
}
