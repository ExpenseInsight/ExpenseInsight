//package com.analytics.expenseinsight.indexing.query.transaction;
//
//import org.elasticsearch.index.query.BoolQueryBuilder;
//import org.elasticsearch.index.query.QueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.springframework.stereotype.Component;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//@Component
//public class TransactionQueryBuilder {
//
//    public QueryBuilder matchByTag(String tag) {
//        return QueryBuilders.matchQuery("tags", tag);
//    }
//
//    public QueryBuilder matchByRecipient(String recipientName) {
//        return QueryBuilders.matchQuery("recipientName", recipientName);
//    }
//
//    public QueryBuilder matchByPaymentType(String paymentType) {
//        return QueryBuilders.matchQuery("paymentType", paymentType);
//    }
//
//    public QueryBuilder filterByAmountRange(BigDecimal min, BigDecimal max) {
//        return QueryBuilders.rangeQuery("amount").gte(min).lte(max);
//    }
//
//    public QueryBuilder filterByDateRange(String startDate, String endDate) {
//        return QueryBuilders.rangeQuery("transactionDate").gte(startDate).lte(endDate);
//    }
//
//    public QueryBuilder searchByText(String text) {
//        return QueryBuilders.multiMatchQuery(text, "recipientName", "description", "tags");
//    }
//
//    public QueryBuilder wildcardTag(String pattern) {
//        return QueryBuilders.wildcardQuery("tags.keyword", pattern.toLowerCase());
//    }
//
//    public QueryBuilder wildcardInDescription(String pattern) {
//        return QueryBuilders.wildcardQuery("description.keyword", "*" + pattern + "*");
//    }
//
//    public QueryBuilder wildcardMultiFieldSearch(String pattern) {
//        return QueryBuilders.queryStringQuery("*" + pattern + "*")
//                .field("recipientName")
//                .field("description")
//                .field("tags");
//    }
//
//    public QueryBuilder combinedSearch(List<String> tags, String paymentType, BigDecimal minAmount, BigDecimal maxAmount, String startDate, String endDate) {
//        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
//
//        for (String tag : tags) {
//            if (tag != null) boolQuery.must(matchByTag(tag));
//        }
//
//        if (paymentType != null) boolQuery.must(matchByPaymentType(paymentType));
//        if (minAmount != null && maxAmount != null) boolQuery.filter(filterByAmountRange(minAmount, maxAmount));
//        if (startDate != null && endDate != null) boolQuery.filter(filterByDateRange(startDate, endDate));
//
//        return boolQuery;
//    }
//}
