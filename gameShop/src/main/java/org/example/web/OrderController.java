package org.example.web;

import lombok.RequiredArgsConstructor;
import org.example.dto.OrderResponse;
import org.example.repositories.UserRepository;
import org.example.services.OrderServiceInterface;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderServiceInterface orderService;
    private final UserRepository userRepository;

    private String getLoggedUserId(Authentication authentication) {
        String login = authentication.getName(); // wyciągnięcie loginu z tokena
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono zalogowanego użytkownika"))
                .getId();
    }

    @PostMapping("/checkout")
    public ResponseEntity<Void> placeOrder(Authentication authentication) {
        String userId = getLoggedUserId(authentication);

        orderService.checkout(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/my-order-history")
    public ResponseEntity<List<OrderResponse>> getOrders(Authentication authentication) {
        String userId = getLoggedUserId(authentication);
        List<OrderResponse> orders = orderService.seeOrderHistory(userId);

        return ResponseEntity.ok(orders);
    }

    @PatchMapping("/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateOrderStatus(@PathVariable("orderId") String orderId, @RequestParam String newStatus) {
        orderService.changeOrderStatus(orderId, newStatus);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/all-orders")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
}
