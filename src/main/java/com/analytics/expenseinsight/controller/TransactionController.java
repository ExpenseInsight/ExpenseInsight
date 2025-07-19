package com.analytics.expenseinsight.controller;

import com.analytics.expenseinsight.model.Transaction;
import com.analytics.expenseinsight.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transactioninfo")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping("create")
    public ResponseEntity<String> createTransaction(@RequestBody Transaction transaction) {
        return transactionService.createTransaction(transaction);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Transaction>> getTransactionsByUserId(@PathVariable int userId) {
        return transactionService.getTransactionsByUserId(userId);
    }


    @GetMapping("/read/{transactionId}")
    public ResponseEntity<Optional<Transaction>> getTransactionById(@PathVariable int transactionId) {
        return transactionService.getTransactionById(transactionId);
    }

    @PutMapping("/update/{transactionId}")
    public ResponseEntity<Transaction> updateTransactionById(
            @PathVariable int transactionId,
            @RequestBody Transaction transaction) {
        return transactionService.updateTransactionById(transactionId, transaction);
    }

    @DeleteMapping("/delete/{transactionId}")
    public ResponseEntity<String> deleteTransactionById(@PathVariable int transactionId) {
        return transactionService.deleteTransactionById(transactionId);
    }
}
