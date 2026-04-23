package com.etudiant.evenements.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginDTO {

    @NotBlank(message = "{error.email.required}")
    @Email(message = "{error.email.invalid}")
    private String email;

    @NotBlank(message = "{error.password.required}")
    private String password;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}