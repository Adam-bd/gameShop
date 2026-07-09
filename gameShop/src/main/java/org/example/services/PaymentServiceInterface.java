package org.example.services;

import org.example.dto.PaymentResponse;

public interface PaymentServiceInterface {
    PaymentResponse createPaymentSession(String orderId, String userId);
}
