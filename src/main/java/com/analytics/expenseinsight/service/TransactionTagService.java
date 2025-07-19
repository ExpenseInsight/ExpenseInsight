package com.analytics.expenseinsight.service;

import com.analytics.expenseinsight.model.Transaction;
import com.analytics.expenseinsight.model.Tag;
import com.analytics.expenseinsight.model.TransactionTag;
import com.analytics.expenseinsight.repository.TransactionRepository;
import com.analytics.expenseinsight.repository.TagRepository;
import com.analytics.expenseinsight.repository.TransactionTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionTagService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TransactionTagRepository transactionTagRepository;

    public ResponseEntity<String> addTagsToTransaction(int transactionId, List<Integer> tagIds) {
        Optional<Transaction> transactionOpt = transactionRepository.findById(transactionId);
        if (transactionOpt.isEmpty()) return new ResponseEntity<>("Transaction not found", HttpStatus.NOT_FOUND);

        Transaction transaction = transactionOpt.get();

        for (int tagId : tagIds) {
            Optional<Tag> tagOpt = tagRepository.findById(tagId);
            if (tagOpt.isPresent()) {
                TransactionTag transactionTag = new TransactionTag();
                transactionTag.setTransaction(transaction);
                transactionTag.setTag(tagOpt.get());
                transactionTagRepository.save(transactionTag);
            }
        }
        return new ResponseEntity<>("Tags added", HttpStatus.CREATED);
    }

    public ResponseEntity<List<TransactionTag>> getTagsForTransaction(int transactionId) {
        Optional<Transaction> transactionOpt = transactionRepository.findById(transactionId);
        if (transactionOpt.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        List<TransactionTag> tags = transactionTagRepository.findByTransaction(transactionOpt.get());
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    public ResponseEntity<String> deleteTagFromTransaction(int transactionId, int tagId) {
        Optional<Transaction> transactionOpt = transactionRepository.findById(transactionId);
        Optional<Tag> tagOpt = tagRepository.findById(tagId);

        if (transactionOpt.isEmpty() || tagOpt.isEmpty()) {
            return new ResponseEntity<>("Transaction or Tag not found", HttpStatus.NOT_FOUND);
        }

        Optional<TransactionTag> transactionTagOpt = transactionTagRepository.findByTransactionAndTag(transactionOpt.get(), tagOpt.get());
        if (transactionTagOpt.isPresent()) {
            transactionTagRepository.delete(transactionTagOpt.get());
            return new ResponseEntity<>("Tag removed from transaction", HttpStatus.OK);
        }

        return new ResponseEntity<>("Tag not associated with transaction", HttpStatus.NOT_FOUND);
    }
}
