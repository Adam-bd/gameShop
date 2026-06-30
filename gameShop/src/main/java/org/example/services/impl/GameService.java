package org.example.services.impl;

import jakarta.transaction.Transactional;
import org.example.dto.GameRequest;
import org.example.dto.GameResponse;
import org.example.models.Game;
import org.example.repositories.GameRepository;
import org.example.services.GameServiceInterface;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class GameService implements GameServiceInterface {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public List<GameResponse> findAllGames() {
        return gameRepository.findAll().stream()
                .map(game -> new GameResponse(
                        game.getId(),
                        game.getTitle(),
                        game.getDescription(),
                        game.getPrice()
                ))
                .toList();
    }

    @Override
    public GameResponse findGameById(String id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Gra nie istnieje"));

        return new GameResponse(game.getId(), game.getTitle(), game.getDescription(), game.getPrice());
    }

    @Override
    public void addGame(GameRequest gameRequest) {
        Game game = Game.builder()
                .title(gameRequest.title())
                .description(gameRequest.description())
                .price(gameRequest.price())
                .genre(gameRequest.genre())
                .build();
        gameRepository.save(game);
    }

    @Override
    public void deleteGame(String id) {
        if (gameRepository.findById(id).isPresent()) {
            gameRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Gra o podanym ID nie istnieje.");
        }
    }

    @Override
    public GameResponse findByTitle(String title) {
        Game game = gameRepository.findByTitle(title)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono gry o tytule: " + title));

        return new GameResponse(game.getId(), game.getTitle(), game.getDescription(), game.getPrice());
    }
}
