package org.example.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString
@Entity
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false, unique = true)
    private String id;

    @Column(name = "game_title", nullable = false)
    private String title;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private java.math.BigDecimal price;

    @Column(name = "genre", nullable = false)
    private String genre;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    public Game copy() {
        return Game.builder()
                .id(id)
                .title(title)
                .price(price)
                .genre(genre)
                .description(description)
                .build();
    }
}
