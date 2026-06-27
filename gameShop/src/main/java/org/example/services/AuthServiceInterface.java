package org.example.services;

import org.example.dto.LoginRequest;
import org.example.dto.LoginResponse;
import org.example.dto.RegisterRequest;
import org.springframework.http.ResponseEntity;

public interface AuthServiceInterface {
    boolean register(RegisterRequest registerRequest);
    LoginResponse login(LoginRequest loginRequest);
}
