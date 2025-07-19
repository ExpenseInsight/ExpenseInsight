package com.analytics.expenseinsight.service;

import com.analytics.expenseinsight.model.Transaction;
import com.analytics.expenseinsight.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    public ResponseEntity<String> createTransaction(Transaction transaction) {
        try {
            transactionRepository.save(transaction);
            return new ResponseEntity<>("Transaction Created Successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<Transaction>> getTransactionsByUserId(int userId) {
        try {
            List<Transaction> transactions = transactionRepository.findByUserUserId(userId);
            return new ResponseEntity<>(transactions, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }


    public ResponseEntity<Optional<Transaction>> getTransactionById(int transactionId) {
        try {
            return new ResponseEntity<>(transactionRepository.findById(transactionId), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Transaction> updateTransactionById(int transactionId, Transaction transaction) {
        try {
            Optional<Transaction> t = transactionRepository.findById(transactionId);
            if (t.isPresent()) {
                Transaction existingTransaction = t.get();

                existingTransaction.setUser(transaction.getUser());
                existingTransaction.setPaymentType(transaction.getPaymentType());
                existingTransaction.setRecipientName(transaction.getRecipientName());
                existingTransaction.setTransactionDate(transaction.getTransactionDate());
                existingTransaction.setPaymentId(transaction.getPaymentId());
                existingTransaction.setAmount(transaction.getAmount());
                existingTransaction.setStatus(transaction.getStatus());
                existingTransaction.setCreatedAt(transaction.getCreatedAt());
                existingTransaction.setUpdatedAt(transaction.getUpdatedAt());

                Transaction savedTransaction = transactionRepository.save(existingTransaction);
                return new ResponseEntity<>(savedTransaction, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<String> deleteTransactionById(int transactionId) {
        try {
            transactionRepository.deleteById(transactionId);
            return new ResponseEntity<>("Transaction Deleted Successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

