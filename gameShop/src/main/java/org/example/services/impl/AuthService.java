package org.example.services.impl;

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
    public boolean register(String login, String rawPassword) {
        if (userRepository.findByLogin(login).isPresent()) {
            throw new IllegalArgumentException("This login is already taken!");
        }

        String hashedPassword = passwordEncoder.encode(rawPassword);

        User user = User.builder().login(login).passwordHash(hashedPassword).role(Role.USER).build();
        userRepository.save(user);
        return true;
    }

    @Override
    public String login(String login, String rawPassword) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(login, rawPassword);

        Authentication authResult = authenticationManager.authenticate(authenticationToken);
        UserDetails userDetails = (UserDetails) authResult.getPrincipal();

        return jwtUtil.generateToken(userDetails);
    }
}
