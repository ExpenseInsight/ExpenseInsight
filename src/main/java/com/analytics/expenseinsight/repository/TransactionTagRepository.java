package com.analytics.expenseinsight.repository;

import com.analytics.expenseinsight.model.TransactionTag;
import com.analytics.expenseinsight.model.Transaction;
import com.analytics.expenseinsight.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionTagRepository extends JpaRepository<TransactionTag, Integer> {
    List<TransactionTag> findByTransaction(Transaction transaction);

    Optional<TransactionTag> findByTransactionAndTag(Transaction transaction, Tag tag);
}
