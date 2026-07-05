package org.example.dto;

import org.example.models.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        String id,
        String userLogin,
        LocalDateTime createdAt,
        OrderStatus status,
        List<OrderItemResponse> items
) {
}
