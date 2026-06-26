package org.example.services.impl;

import jakarta.transaction.Transactional;
import org.example.models.User;
import org.example.repositories.UserRepository;
import org.example.services.UserServiceInterface;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserService implements UserServiceInterface {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findById(String userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(String toDeleteUserId, String loggedInUserId) {
        if(userRepository.findById(toDeleteUserId).isEmpty()) {
            throw new IllegalArgumentException("Użytkownik o podanym ID nie istnieje.");
        }
        if(toDeleteUserId.equals(loggedInUserId)) {
            throw new IllegalStateException("Nie możesz usunąć samego siebie gdy jesteś zalogowany/na.");
        }
        userRepository.deleteById(toDeleteUserId);
    }

    @Override
    public User findByLogin(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono użytkownika o loginie: " + login));
    }
}