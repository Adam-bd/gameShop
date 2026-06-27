package org.example.services.impl;

import org.example.dto.LoginRequest;
import org.example.dto.LoginResponse;
import org.example.dto.RegisterRequest;
import org.example.models.Role;
import org.example.models.User;
import org.example.repositories.UserRepository;
import org.example.security.JwtUtil;
import org.example.services.AuthServiceInterface;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements AuthServiceInterface {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean register(RegisterRequest registerRequest) {
        if (userRepository.findByLogin(registerRequest.login()).isPresent()) {
            throw new IllegalArgumentException("This login is already taken!");
        }

        String hashedPassword = passwordEncoder.encode(registerRequest.password());

        User user = User.builder().login(registerRequest.login()).passwordHash(hashedPassword).role(Role.USER).build();
        userRepository.save(user);
        return true;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {

        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.login(),
                loginRequest.password()
        ));

        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        String token = jwtUtil.generateToken(userDetails);
        return new LoginResponse(token);
    }
}
