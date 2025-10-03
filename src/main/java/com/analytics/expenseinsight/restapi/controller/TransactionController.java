package com.analytics.expenseinsight.restapi.controller;

import com.analytics.expenseinsight.indexing.model.TransactionIndexDTO;
import com.analytics.expenseinsight.indexing.service.transaction.TransactionIndexService;
import com.analytics.expenseinsight.restapi.model.Transaction;
import com.analytics.expenseinsight.restapi.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transactioninfo")
public class TransactionController {

    @Autowired
    TransactionService transactionService;
    @Autowired
    TransactionIndexService transactionIndexService;

    @PostMapping("create")
    public ResponseEntity<String> createTransaction(@RequestBody Transaction transaction) throws IOException {
        TransactionIndexDTO dto = new TransactionIndexDTO(transaction);
        try {
            transactionIndexService.indexTransaction(dto);
        } catch (Exception e) {
            return new ResponseEntity<>("Transaction Not added to Index " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

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
