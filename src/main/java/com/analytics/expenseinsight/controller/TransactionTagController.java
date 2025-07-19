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

    @PostMapping("/{id}/tags")
    public ResponseEntity<String> addTagsToTransaction(@PathVariable("id") int transactionId,
                                                       @RequestBody List<Integer> tagIds) {
        return transactionTagService.addTagsToTransaction(transactionId, tagIds);
    }

    @GetMapping("/{id}/tags")
    public ResponseEntity<List<TransactionTag>> getTagsForTransaction(@PathVariable("id") int transactionId) {
        return transactionTagService.getTagsForTransaction(transactionId);
    }

    @DeleteMapping("/{id}/tags/{tagId}")
    public ResponseEntity<String> removeTagFromTransaction(@PathVariable("id") int transactionId,
                                                           @PathVariable("tagId") int tagId) {
        return transactionTagService.deleteTagFromTransaction(transactionId, tagId);
    }
}
