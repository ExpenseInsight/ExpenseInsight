package com.analytics.expenseinsight.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "transaction_tags")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TransactionTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int transactionTagId;

    @ManyToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    @ManyToOne
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;
}
