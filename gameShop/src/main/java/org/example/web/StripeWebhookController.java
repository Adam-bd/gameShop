package org.example.web;

import com.stripe.exception.EventDataObjectDeserializationException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.example.services.OrderServiceInterface;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StripeWebhookController {

    private final OrderServiceInterface orderService;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @PostMapping("/api/webhook")
    public ResponseEntity<String> webhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) throws EventDataObjectDeserializationException {
        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        }

//        if ("checkout.session.completed".equals(event.getType())) {
//            event.getDataObjectDeserializer().getObject().ifPresent(stripeObject -> {
//                Session session = (Session) stripeObject;
//                String orderId = session.getMetadata().get("orderId");
//                if (orderId != null) {
//                    orderService.changeOrderStatus(orderId, "PAID");
//                }
//            });
//        }
        if ("checkout.session.completed".equals(event.getType())) {
            System.out.println("-> Odebrano event checkout.session.completed!");

            // 1. Wyciągamy obiekt z eventu (bezpiecznie lub siłowo)
            var deserializer = event.getDataObjectDeserializer();
            com.stripe.model.StripeObject stripeObject;

            if (deserializer.getObject().isPresent()) {
                stripeObject = deserializer.getObject().get();
            } else {
                // To ta magiczna linijka, która rozwiązuje Twój problem!
                System.out.println("-> Różnica wersji API. Wymuszam deserializację (deserializeUnsafe)...");
                stripeObject = deserializer.deserializeUnsafe();
            }

            // 2. Rzutujemy na sesję i wyciągamy ID
            Session session = (Session) stripeObject;
            String orderId = session.getMetadata().get("orderId");
            System.out.println("-> Wyciągnięte orderId z metadanych: " + orderId);

            // 3. Zmiana statusu
            if (orderId != null) {
                orderService.changeOrderStatus(orderId, "PAID");
                System.out.println("-> Status zamówienia zmieniony na PAID w bazie!");
            } else {
                System.out.println("-> BŁĄD: Brak orderId w metadanych!");
            }
        }
        return ResponseEntity.ok().build();
    }
}
