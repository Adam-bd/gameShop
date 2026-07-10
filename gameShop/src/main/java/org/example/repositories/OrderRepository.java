package org.example.repositories;

import org.example.models.Order;
import org.example.models.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findAllByUserId(String userId);
    List<Order> findAllByStatusAndCreatedAtBefore(OrderStatus status, LocalDateTime dateTime);
}
