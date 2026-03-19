package com.example.api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;       // stored as BCrypt hash, never plain text

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)   // store "ADMIN"/"USER" as strings, not 0/1
    private Role role;

    public enum Role {
        USER, ADMIN
    }
}