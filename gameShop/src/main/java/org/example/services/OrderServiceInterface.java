package org.example.services;

import org.example.dto.OrderResponse;
import org.example.repositories.CartRepository;

import java.util.List;

public interface OrderServiceInterface {
    void checkout(String userId);

    List<OrderResponse> seeOrderHistory(String userId);

    void changeOrderStatus(String orderId, String newStatus);

    List<OrderResponse> getAllOrders();
}
