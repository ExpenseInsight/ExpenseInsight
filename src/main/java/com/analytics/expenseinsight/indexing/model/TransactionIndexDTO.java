package com.analytics.expenseinsight.indexing.model;

import com.analytics.expenseinsight.restapi.model.Tag;
import com.analytics.expenseinsight.restapi.model.Transaction;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class TransactionIndexDTO {
    private int id;
    private String paymentType;
    private String recipientName;
    private BigDecimal amount;
    private String status;
    private Date transactionDate;
    private String description;
    private List<String> tags;

    public TransactionIndexDTO (Transaction transaction){
        this.id = transaction.getTransactionId();
        this.paymentType = transaction.getPaymentType().name();
        this.recipientName = transaction.getRecipientName();
        this.amount = transaction.getAmount();
        this.status = transaction.getStatus();
        this.transactionDate = transaction.getTransactionDate();
        this.description = transaction.getDescription();
        this.tags = getTagsList(transaction.getTags());
    }

    public static List<String> getTagsList (List<Tag> tags) {
        List<String> tagList =  new ArrayList<>();
        for(Tag t : tags ){
            tagList.add(t.getTagName()+"_ " + t.getTagId());
        }

        return tagList;
    }

}
