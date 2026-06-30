package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record GameRequest(
        @NotBlank(message = "Tytuł gry nie może być pusty")
        String title,

        String description,

        @Positive(message = "Cena musi być większa od zera")
        BigDecimal price,

        String genre
) {
}