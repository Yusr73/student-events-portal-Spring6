package com.etudiant.evenements.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{error.nom.required}")
    @Column(nullable = false)
    private String nom;

    @NotBlank(message = "{error.email.required}")
    @Email(message = "{error.email.invalid}")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "{error.password.required}")
    @Size(min = 6, message = "{error.password.length}")
    @Column(nullable = false)
    private String password;

    // Constructors
    public User() {}

    public User(String nom, String email, String password) {
        this.nom = nom;
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}