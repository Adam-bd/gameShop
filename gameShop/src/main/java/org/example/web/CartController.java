package org.example.web;

import lombok.RequiredArgsConstructor;
import org.example.dto.CartResponse;
import org.example.repositories.UserRepository;
import org.example.services.CartServiceInterface;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartServiceInterface cartService;
    private final UserRepository userRepository;

    private String getLoggedUserId(Authentication authentication) {
        String login = authentication.getName(); // wyciągnięcie loginu z tokena
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono zalogowanego użytkownika"))
                .getId();
    }

    @PostMapping("/add/{gameId}")
    public ResponseEntity<Void> addGameToCart(
            @PathVariable String gameId,
            @RequestParam(defaultValue = "1") int quantity,
            Authentication authentication) {

        String userId = getLoggedUserId(authentication);
        cartService.addGameToCart(gameId, quantity, userId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/remove/{gameId}")
    public ResponseEntity<Void> removeGameFromCart(
            @PathVariable String gameId,
            @RequestParam(defaultValue = "1") int quantity,
            Authentication authentication) {

        String userId = getLoggedUserId(authentication);
        cartService.deleteGameFromCart(gameId, quantity, userId);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart(Authentication authentication) {
        String userId = getLoggedUserId(authentication);
        CartResponse cartResponse = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cartResponse);
    }
}