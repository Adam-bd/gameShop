package org.example.services;

import org.example.dto.GameRequest;
import org.example.dto.GameResponse;
import org.example.models.Game;

import java.util.List;

public interface GameServiceInterface {
    List<GameResponse> findAllGames();

    GameResponse findGameById(String id);

    void addGame(GameRequest gameRequest);

    void deleteGame(String id);

    GameResponse findByTitle(String title);

}
