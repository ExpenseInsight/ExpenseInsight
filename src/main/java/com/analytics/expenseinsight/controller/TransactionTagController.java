package com.analytics.expenseinsight.controller;

import com.analytics.expenseinsight.model.TransactionTag;
import com.analytics.expenseinsight.service.TransactionTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactionsTag")
public class TransactionTagController {

    @Autowired
    private TransactionTagService transactionTagService;

    @PostMapping("/{id}")
    public ResponseEntity<String> addTagsToTransaction(@PathVariable("id") int transactionId,
                                                       @RequestBody List<Integer> tagIds) {
        return transactionTagService.addTagsToTransaction(transactionId, tagIds);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<List<TransactionTag>> getTagsForTransaction(@PathVariable int transactionId) {
        return transactionTagService.getTagsForTransaction(transactionId);
    }

    @DeleteMapping("/{transactionId}/{tagId}")
    public ResponseEntity<String> removeTagFromTransaction(@PathVariable int transactionId,
                                                           @PathVariable int tagId) {
        return transactionTagService.deleteTagFromTransaction(transactionId, tagId);
    }
}
