package com.etudiant.evenements.validator;

import com.etudiant.evenements.entity.User;
import com.etudiant.evenements.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        // Check if nom is empty - using message key only (no hardcoded text)
        if (user.getNom() == null || user.getNom().trim().isEmpty()) {
            errors.rejectValue("nom", "error.nom.required");
        }

        // Check email
        String email = user.getEmail();
        if (email == null || email.trim().isEmpty()) {
            errors.rejectValue("email", "error.email.required");
        } else if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errors.rejectValue("email", "error.email.invalid");
        } else if (userRepository.existsByEmail(email)) {
            errors.rejectValue("email", "error.email.exists");
        }

        // Check password
        String password = user.getPassword();
        if (password == null || password.isEmpty()) {
            errors.rejectValue("password", "error.password.required");
        } else if (password.length() < 6) {
            errors.rejectValue("password", "error.password.length");
        }
    }
}