package org.example.services.impl;

import jakarta.transaction.Transactional;
import org.example.dto.OrderItemResponse;
import org.example.dto.OrderResponse;
import org.example.models.*;
import org.example.repositories.CartRepository;
import org.example.repositories.OrderRepository;
import org.example.repositories.UserRepository;
import org.example.services.OrderServiceInterface;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class OrderService implements OrderServiceInterface {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    public OrderService(UserRepository userRepository, OrderRepository orderRepository, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    public void checkout(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("You can't place an order with an empty cart");
        }

        Order order = Order.builder()
                .user(user)
                .createdAt(LocalDateTime.now())
                .status(OrderStatus.NEW)
                .build();

        cart.getItems().forEach(item -> order.addItem(
                OrderItem.builder()
                        .game(item.getGame())
                        .gameTitle(item.getGame().getTitle())
                        .unitPrice(item.getGame().getPrice())
                        .quantity(item.getQuantity())
                        .build()
        ));

        orderRepository.save(order);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Override
    public List<OrderResponse> seeOrderHistory(String userId) {
        List<Order> orders = orderRepository.findAllByUserId(userId);

        return orders.stream()
                .map(order -> new OrderResponse(
                        order.getId(),
                        order.getUser().getLogin(),
                        order.getCreatedAt(),
                        order.getStatus(),
                        order.getItems().stream()
                                .map(item -> new OrderItemResponse(
                                        item.getId(),
                                        item.getGameTitle(),
                                        item.getUnitPrice(),
                                        item.getQuantity()
                                ))
                                .toList()
                ))
                .toList();
    }

    @Override
    public void changeOrderStatus(String orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(OrderStatus.valueOf(newStatus));
        orderRepository.save(order);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(order -> new OrderResponse(
                        order.getId(),
                        order.getUser().getLogin(),
                        order.getCreatedAt(),
                        order.getStatus(),
                        order.getItems().stream()
                                .map(item -> new OrderItemResponse(
                                        item.getId(),
                                        item.getGameTitle(),
                                        item.getUnitPrice(),
                                        item.getQuantity()
                                ))
                                .toList()
                ))
                .toList();
    }
}
