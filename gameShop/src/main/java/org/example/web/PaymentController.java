package org.example.web;

import com.stripe.net.Webhook;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.example.dto.PaymentResponse;
import org.example.repositories.UserRepository;
import org.example.services.OrderServiceInterface;
import org.example.services.PaymentServiceInterface;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentServiceInterface paymentService;
    private final OrderServiceInterface orderService;
    private final UserRepository userRepository;

    private String getLoggedUserId(Authentication authentication) {
        String login = authentication.getName();
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono zalogowanego użytkownika"))
                .getId();
    }

    @PostMapping("/create-session/{orderId}")
    public ResponseEntity<PaymentResponse> createSession(@PathVariable String orderId, Authentication authentication) {
        String userId = getLoggedUserId(authentication);
        PaymentResponse response = paymentService.createPaymentSession(orderId, userId);

        return ResponseEntity.ok(response);
    }

//    @GetMapping("/success")
//    public ResponseEntity<String> paymentSuccess(@RequestParam String orderId) {
//        return ResponseEntity.ok("Płatność zakończona sukcesem! Status zamówienia zaktualizowany.");
//    }
//
//    @GetMapping("/cancel")
//    public ResponseEntity<String> paymentCancel() {
//        return ResponseEntity.ok("Płatność została anulowana. Możesz spróbować ponownie później.");
//    }

    @GetMapping(value = "/success", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> paymentSuccess() {
        String html = """
                <!DOCTYPE html>
                <html lang="pl">
                <head>
                    <meta charset="UTF-8">
                    <title>Sukces!</title>
                    <style>
                        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f3f4f6; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }
                        .card { background-color: white; padding: 40px; border-radius: 10px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); text-align: center; max-width: 400px; }
                        .icon { font-size: 50px; margin-bottom: 20px; }
                        h1 { color: #10b981; margin: 0 0 10px 0; font-size: 24px; }
                        p { color: #6b7280; font-size: 16px; line-height: 1.5; }
                    </style>
                </head>
                <body>
                    <div class="card">
                        <div class="icon">✅</div>
                        <h1>Płatność zaakceptowana!</h1>
                        <p>Twoje zamówienie zostało pomyślnie opłacone i trafiło do realizacji. Dziękujemy za zakupy w naszym sklepie!</p>
                    </div>
                </body>
                </html>
                """;
        return ResponseEntity.ok(html);
    }

    @GetMapping(value = "/cancel", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> paymentCancel() {
        String html = """
                <!DOCTYPE html>
                <html lang="pl">
                <head>
                    <meta charset="UTF-8">
                    <title>Anulowano</title>
                    <style>
                        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f3f4f6; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }
                        .card { background-color: white; padding: 40px; border-radius: 10px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); text-align: center; max-width: 400px; }
                        .icon { font-size: 50px; margin-bottom: 20px; }
                        h1 { color: #ef4444; margin: 0 0 10px 0; font-size: 24px; }
                        p { color: #6b7280; font-size: 16px; line-height: 1.5; }
                    </style>
                </head>
                <body>
                    <div class="card">
                        <div class="icon">❌</div>
                        <h1>Płatność anulowana</h1>
                        <p>Transakcja została przerwana. Twoje zamówienie wciąż czeka w systemie, możesz spróbować opłacić je ponownie później.</p>
                    </div>
                </body>
                </html>
                """;
        return ResponseEntity.ok(html);
    }
}