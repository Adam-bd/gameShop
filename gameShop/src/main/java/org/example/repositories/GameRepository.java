package org.example.repositories;

import org.example.models.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, String> {
    Optional<Game> findByTitle(String title);
}
