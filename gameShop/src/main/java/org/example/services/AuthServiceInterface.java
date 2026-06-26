package org.example.services;

public interface AuthServiceInterface {
    boolean register(String login, String rawPassword);
    String login(String login, String rawPassword);
}
