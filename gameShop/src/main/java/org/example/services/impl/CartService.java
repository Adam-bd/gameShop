package org.example.services.impl;

import jakarta.transaction.Transactional;
import org.example.models.Cart;
import org.example.models.CartItem;
import org.example.models.User;
import org.example.repositories.CartRepository;
import org.example.repositories.GameRepository;
import org.example.repositories.UserRepository;
import org.example.services.CartServiceInterface;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CartService implements CartServiceInterface {

    private final CartRepository cartRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepository, GameRepository gameRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void addGameToCart(String gameId, int quantity, String userId) {
        User user;
        if (userRepository.findById(userId).isPresent()) {
            user = userRepository.findById(userId).get();
        } else {
            throw new RuntimeException("User not found");
        }

        Cart cart = new Cart();
        if (cartRepository.findByUserId(userId).isEmpty()) {
            cart.setUser(user);
            cartRepository.save(cart);
        } else {
            // ?
        }

        if (gameRepository.findById(gameId).isEmpty()) {
            throw new RuntimeException("Game not found");
        }

        cart.getItems().stream()
                .filter(item -> item.getGame().getId().equals(gameId))
                .findFirst()
                .ifPresentOrElse(item -> item.setQuantity(item.getQuantity() + quantity),
                        () -> CartItem.builder().id().quantity().cart().game().build()
                );

        cartRepository.save(cart);
    }

    @Override
    public void deleteGameFromCart(String gameId, int quantity, String userId) {

    }
}
