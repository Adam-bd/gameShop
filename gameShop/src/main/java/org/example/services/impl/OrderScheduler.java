package org.example.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.models.Order;
import org.example.models.OrderStatus;
import org.example.repositories.OrderRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderScheduler {

    private final OrderRepository orderRepository;

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void cancelUnpaidOrders() {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(5); //testowe 5 minut

        List<Order> expiredOrders = orderRepository.findAllByStatusAndCreatedAtBefore(OrderStatus.NEW, threshold);

        if (!expiredOrders.isEmpty()) {
            System.out.println("-> [Scheduler] Znaleziono " + expiredOrders.size() + " nieopłaconych zamówień. Rozpoczynam anulowanie...");

            for (Order order : expiredOrders) {
                order.setStatus(OrderStatus.CANCELLED);
                // Tutaj można dodać zwracanie gry do puli dostępnych gier -> do rozbudowania
                System.out.println("-> Anulowano zamówienie ID: " + order.getId());
            }
        }
    }
}