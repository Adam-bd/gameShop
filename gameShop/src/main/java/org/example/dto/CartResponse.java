package org.example.dto;

import java.util.List;

public record CartResponse(
        String id,
        String userLogin,
        List<CartItemResponse> items
) {
}
