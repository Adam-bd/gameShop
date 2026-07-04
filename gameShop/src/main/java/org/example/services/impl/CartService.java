package org.example.services.impl;

import jakarta.transaction.Transactional;
import org.example.dto.CartItemResponse;
import org.example.dto.CartResponse;
import org.example.models.Cart;
import org.example.models.CartItem;
import org.example.models.Game;
import org.example.models.User;
import org.example.repositories.CartRepository;
import org.example.repositories.GameRepository;
import org.example.repositories.UserRepository;
import org.example.services.CartServiceInterface;
import org.springframework.stereotype.Service;

import java.util.List;

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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });

        cart.getItems().stream()
                .filter(item -> item.getGame().getId().equals(gameId))
                .findFirst()
                .ifPresentOrElse(item -> item.setQuantity(item.getQuantity() + quantity),
                        () -> {
                            CartItem cartItem = CartItem.builder()
                                    .quantity(quantity)
                                    .cart(cart)
                                    .game(game)
                                    .build();
                            cart.addItem(cartItem);
                        }
                );

        cartRepository.save(cart);
    }

    @Override
    public void deleteGameFromCart(String gameId, int quantity, String userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getItems().stream()
                .filter(item -> item.getGame().getId().equals(gameId))
                .findFirst()
                .ifPresent(item -> {
                    int newQuantity = item.getQuantity() - quantity;
                    if (newQuantity <= 0) {
                        cart.removeItem(item);
                    } else {
                        item.setQuantity(newQuantity);
                    }
                });

        cartRepository.save(cart);
    }

    @Override
    public CartResponse getCartByUserId(String userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        List<CartItemResponse> items = cart.getItems().stream()
                .map(item -> new CartItemResponse(
                        item.getId(),
                        item.getGame().getId(),
                        item.getGame().getTitle(),
                        item.getQuantity(),
                        item.getGame().getPrice()
                ))
                .toList();

        return new CartResponse(cart.getId(), cart.getUser().getLogin(), items);
    }
}
