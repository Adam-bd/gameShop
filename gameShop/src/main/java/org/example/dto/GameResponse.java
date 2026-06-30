package org.example.dto;

import java.math.BigDecimal;

public record GameResponse(
        String id,
        String title,
        String description,
        BigDecimal price
) {
}