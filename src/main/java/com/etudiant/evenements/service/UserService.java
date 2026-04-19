package com.etudiant.evenements.service;

import com.etudiant.evenements.model.User;
import com.etudiant.evenements.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    public boolean register(String nom, String email, String password) {
        if (!isValidEmail(email)) {
            return false;
        }
        if (!isValidPassword(password)) {
            return false;
        }
        if (userRepository.existsByEmail(email)) {
            return false;
        }

        // Hash the password before saving
        String hashedPassword = encoder.encode(password);
        User user = new User(nom, email, hashedPassword);
        userRepository.save(user);
        return true;
    }

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && encoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    // Method for remember me functionality
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}