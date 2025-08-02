package com.analytics.expenseinsight.indexing.service.transaction;

import com.analytics.expenseinsight.indexing.query.transaction.TransactionQueryBuilder;
import com.analytics.expenseinsight.indexing.source.transaction.TransactionSourceBuilder;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionSearchService {

    private final RestHighLevelClient client;
    private final TransactionQueryBuilder queryBuilder;
    private final TransactionSourceBuilder sourceBuilder;

    public SearchResponse searchByTag(String indexName, String tag) throws IOException {
        QueryBuilder query = queryBuilder.matchByTag(tag);
        return executeSearch(indexName, sourceBuilder.buildSource(query));
    }

    public SearchResponse wildcardSearchByTag(String indexName, String tagPattern) throws IOException {
        QueryBuilder query = queryBuilder.wildcardTag(tagPattern);
        return executeSearch(indexName, sourceBuilder.buildSource(query));
    }

    public SearchResponse wildcardSearchByDescription(String indexName , String pattern) throws IOException {
        QueryBuilder query = queryBuilder.wildcardInDescription(pattern);
        return executeSearch(indexName, sourceBuilder.buildSource(query));
    }

    public SearchResponse wildcardMultiFieldSearch(String indexName , String pattern) throws IOException {
        QueryBuilder query = queryBuilder.wildcardMultiFieldSearch(pattern);
        return executeSearch(indexName, sourceBuilder.buildSource(query));
    }

    public SearchResponse searchByRecipient(String indexName, String recipientName) throws IOException {
        QueryBuilder query = queryBuilder.matchByRecipient(recipientName);
        return executeSearch(indexName, sourceBuilder.buildSource(query));
    }

    public SearchResponse searchByDateRange(String indexName, String startDate, String endDate) throws IOException {
        QueryBuilder query = queryBuilder.filterByDateRange(startDate, endDate);
        return executeSearch(indexName, sourceBuilder.buildSource(query));
    }

    public SearchResponse searchByText(String indexName, String text) throws IOException {
        QueryBuilder query = queryBuilder.searchByText(text);
        return executeSearch(indexName, sourceBuilder.buildSource(query));
    }

    public SearchResponse combinedSearch(String indexName, String tag, String paymentType,
                                         String startDate, String endDate,
                                         double minAmount, double maxAmount) throws IOException {
        QueryBuilder query = queryBuilder.combinedSearch(tag, paymentType,
                BigDecimal.valueOf(minAmount), BigDecimal.valueOf(maxAmount),
                startDate, endDate);
        return executeSearch(indexName, sourceBuilder.buildSource(query));
    }

    public SearchResponse paginatedSearch(String indexName, QueryBuilder query, int pageSize, Object[] searchAfter, List<String>sortFileds , List<SortOrder> sortOrders) throws IOException {
        SearchSourceBuilder builder = sourceBuilder.buildPaginatedSortedSource(query, pageSize, searchAfter , sortFileds , sortOrders);
        return executeSearch(indexName, builder);
    }

    private SearchResponse executeSearch(String indexName, SearchSourceBuilder builder) throws IOException {
        SearchRequest request = new SearchRequest(indexName);
        request.source(builder);
        return client.search(request, RequestOptions.DEFAULT);
    }

}
