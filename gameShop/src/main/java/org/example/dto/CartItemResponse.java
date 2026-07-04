package org.example.dto;

import java.math.BigDecimal;

public record CartItemResponse(
        String id,
        String gameId,
        String gameTitle,
        int quantity,
        BigDecimal unitPrice
) {
}
