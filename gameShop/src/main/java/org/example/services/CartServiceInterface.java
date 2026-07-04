package org.example.services;

public interface CartServiceInterface {
    void addGameToCart(String gameId, int quantity, String userId);
    void deleteGameFromCart(String gameId, int quantity, String userId);
}
