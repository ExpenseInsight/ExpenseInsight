package com.analytics.expenseinsight.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Recommended for MySQL
    private int userId;

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String userName;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String passwordHash;

    @Digits(integer = 10, fraction = 0, message = "Phone number must be 10 digits")
    @Column(unique = true)
    private long phoneNumber;

    @PastOrPresent(message = "CreatedAt must be in the past or present")
    private LocalDateTime createdAt;

    @PastOrPresent(message = "UpdatedAt must be in the past or present")
    private LocalDateTime updatedAt;

}
