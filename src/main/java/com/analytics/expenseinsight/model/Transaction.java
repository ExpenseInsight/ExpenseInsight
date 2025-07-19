package com.analytics.expenseinsight.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int transactionId;

    // 🔗 Foreign key to User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 🔁 Sent or Received
    @NotNull(message = "Payment type is required")
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    // 🧾 Recipient
    @NotBlank(message = "Recipient name is required")
    private String recipientName;

    // 📅 Date of transaction
    @NotNull(message = "Transaction date is required")
    private LocalDateTime transactionDate;

    // 🔢 Reference ID
    @NotBlank(message = "Payment ID is required")
    private String paymentId;

    // 📊 Amount
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    // 🟢 Status
    @NotBlank(message = "Status is required")
    private String status;

    // 🕒 Timestamps
    @PastOrPresent
    private LocalDateTime createdAt;

    @PastOrPresent
    private LocalDateTime updatedAt;

    // Auto-set timestamps
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
