package com.example.onlineshop.repository;

import com.example.onlineshop.entity.OrderEntity;
import com.example.onlineshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByUser(User user);
    Optional<OrderEntity> findByIdAndUser(Long id, User user);
}
