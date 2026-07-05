package org.example.dto;

import java.math.BigDecimal;

public record OrderItemResponse(
        String id,
        String gameTitle,
        BigDecimal unitPrice,
        int quantity
) {
}
