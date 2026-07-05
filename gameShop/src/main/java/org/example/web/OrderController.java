package org.example.web;

import lombok.RequiredArgsConstructor;
import org.example.repositories.CartRepository;
import org.example.repositories.UserRepository;
import org.example.services.OrderServiceInterface;
import org.example.services.impl.CartService;
import org.example.services.impl.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderServiceInterface orderService;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    private String getLoggedUserId(Authentication authentication) {
        String login = authentication.getName(); // wyciągnięcie loginu z tokena
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono zalogowanego użytkownika"))
                .getId();
    }


}
