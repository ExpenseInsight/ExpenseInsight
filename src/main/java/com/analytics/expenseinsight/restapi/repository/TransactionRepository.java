package com.analytics.expenseinsight.restapi.repository;

import com.analytics.expenseinsight.restapi.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findByUserUserId(int userId);

}
