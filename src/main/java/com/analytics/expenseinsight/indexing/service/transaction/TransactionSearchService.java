package com.analytics.expenseinsight.indexing.service.transaction;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import com.analytics.expenseinsight.indexing.model.TransactionIndexDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionSearchService {

    private final ElasticsearchClient client;
    private static final String INDEX_PREFIX = "transactions_user_";

    // ========== BASIC SEARCH ========== //

    public List<TransactionIndexDTO> searchTransactions(int userId, String query) throws IOException {
        String indexName = getIndexName(userId);

        SearchResponse<TransactionIndexDTO> response = client.search(s -> s
                        .index(indexName)
                        .query(q -> q
                                .multiMatch(m -> m
                                        .query(query)
                                        .fields("description", "recipientName", "tags", "paymentType")
                                        .fuzziness("AUTO")
                                )
                        ),
                TransactionIndexDTO.class
        );

        return extractResults(response);
    }

    // ========== FILTER-BASED SEARCH ========== //

//    public List<TransactionIndexDTO> filterByAmountRange(int userId, double minAmount, double maxAmount) throws IOException {
//        String indexName = getIndexName(userId);
//
//        // Build the range query separately
//        RangeQuery rangeQuery = RangeQuery.of(r -> r
//                .field("amount")
//                .gte(JsonData.of(minAmount))
//                .lte(JsonData.of(maxAmount))
//        );
//
//        SearchResponse<TransactionIndexDTO> response = client.search(s -> s
//                        .index(indexName)
//                        .query(q -> q.range(rangeQuery)),
//                TransactionIndexDTO.class
//        );
//
//        return extractResults(response);
//    }

//    public List<TransactionIndexDTO> filterByDateRange(int userId, LocalDate startDate, LocalDate endDate) throws IOException {
//        String indexName = getIndexName(userId);
//
//        SearchResponse<TransactionIndexDTO> response = client.search(s -> s
//                        .index(indexName)
//                        .query(q -> q
//                                .range(r -> r
//                                        .field("transactionDate")
//                                        .gte(JsonData.of(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()))
//                                        .lte(JsonData.of(endDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()))
//                                )
//                        ),
//                TransactionIndexDTO.class
//        );
//
//        return extractResults(response);
//    }

    public List<TransactionIndexDTO> filterByStatus(int userId, String status) throws IOException {
        String indexName = getIndexName(userId);

        SearchResponse<TransactionIndexDTO> response = client.search(s -> s
                        .index(indexName)
                        .query(q -> q
                                .term(t -> t
                                        .field("status")
                                        .value(v -> v.stringValue(status))
                                )
                        ),
                TransactionIndexDTO.class
        );

        return extractResults(response);
    }

    // ========== TAG-BASED SEARCH ========== //

    public List<TransactionIndexDTO> searchByAllTags(int userId, List<String> tags) throws IOException {
        String indexName = getIndexName(userId);

        SearchResponse<TransactionIndexDTO> response = client.search(s -> s
                        .index(indexName)
                        .query(q -> q
                                .bool(b -> b
                                        .must(tags.stream()
                                                .map(tag -> Query.of(q2 -> q2
                                                        .term(t -> t
                                                                .field("tags")
                                                                .value(v -> v.stringValue(tag))
                                                        )
                                                ))
                                                .toList()
                                        )
                                )
                        ),
                TransactionIndexDTO.class
        );

        return extractResults(response);
    }

    public List<TransactionIndexDTO> searchByAnyTags(int userId, List<String> tags) throws IOException {
        String indexName = getIndexName(userId);

        SearchResponse<TransactionIndexDTO> response = client.search(s -> s
                        .index(indexName)
                        .query(q -> q
                                .terms(t -> t
                                        .field("tags")
                                        .terms(t2 -> t2
                                                .value(tags.stream()
                                                        .map(tag -> FieldValue.of(tag))
                                                        .toList()
                                                )
                                        )
                                )
                        ),
                TransactionIndexDTO.class
        );

        return extractResults(response);
    }

    // ========== HELPER METHODS ========== //

    private String getIndexName(int userId) {
        return INDEX_PREFIX + userId;
    }

    private List<TransactionIndexDTO> extractResults(SearchResponse<TransactionIndexDTO> response) {
        return response.hits().hits().stream()
                .map(Hit::source)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
