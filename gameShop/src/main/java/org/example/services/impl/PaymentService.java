package org.example.services.impl;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.example.dto.PaymentResponse;
import org.example.models.Order;
import org.example.models.OrderStatus;
import org.example.models.User;
import org.example.repositories.OrderRepository;
import org.example.repositories.UserRepository;
import org.example.services.PaymentServiceInterface;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class PaymentService implements PaymentServiceInterface {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Value("${app.base.url}")
    private String baseUrl;

    public PaymentService(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    @Override
    public PaymentResponse createPaymentSession(String orderId, String userId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        if (!order.getUser().equals(user)) {
            throw new RuntimeException("User not authorized for this order");
        }

        if (order.getStatus().equals(OrderStatus.valueOf("PAID"))) {
            throw new RuntimeException("Order is already paid");
        }

        // Mapowanie pozycji z zamówienia na format Stripe'a
        List<SessionCreateParams.LineItem> lineItems = order.getItems().stream()
                .map(item -> SessionCreateParams.LineItem.builder()
                        .setQuantity((long) item.getQuantity())
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("pln")
                                // podanie kwoty w groszach, ponieważ Stripe tego wymaga
                                .setUnitAmount(item.getUnitPrice().multiply(BigDecimal.valueOf(100)).longValue())
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName(item.getGameTitle())
                                        .build())
                                .build())
                        .build())
                .toList();

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.BLIK)
                .setSuccessUrl(baseUrl + "/api/payment/success")
                .setCancelUrl(baseUrl + "/api/payment/cancel")
                .addAllLineItem(lineItems)
                .putMetadata("orderId", orderId) // przekazujemy Stripe z jakim order ma połączyć tą płatność
                .build();

        try {
            Session session = Session.create(params);
            return new PaymentResponse(session.getUrl());
        } catch (StripeException e) {
            throw new RuntimeException("Błąd podczas łączenia z systemem płatności: " + e.getMessage());
        }
    }
}
