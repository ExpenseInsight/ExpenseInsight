package com.analytics.expenseinsight.indexing.service.transaction;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import com.analytics.expenseinsight.indexing.helper.SearchConstants;
import com.analytics.expenseinsight.indexing.model.TransactionIndexDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransactionIndexService {

    private final ElasticsearchClient client;

    // ========== CRUD Operations ========== //

    /**
     * Creates a user-specific index if it doesn't exist
     */
    private void ensureUserIndexExists(int userId) throws IOException {
        String indexName = getIndexName(userId);

        boolean exists = client.indices()
                .exists(ExistsRequest.of(e -> e.index(indexName)))
                .value();

        if (!exists) {
            CreateIndexResponse response = client.indices()
                    .create(c -> c.index(indexName));

            System.out.println("Created index: " + response.index());
        }
    }

    /**
     * Index a transaction (Create/Update)
     */
    public String indexTransaction(TransactionIndexDTO transaction) throws IOException {
        int userId = transaction.getUserId();
        ensureUserIndexExists(userId);

        String indexName = getIndexName(userId);

        IndexResponse response = client.index(i -> i
                .index(indexName)
                .id(String.valueOf(transaction.getId()))
                .document(transaction)
        );

        return response.id();
    }

    /**
     * Get transaction by ID
     */
    public TransactionIndexDTO getTransaction(int userId, int transactionId) throws IOException {
        String indexName = getIndexName(userId);

        GetResponse<TransactionIndexDTO> response = client.get(g -> g
                        .index(indexName)
                        .id(String.valueOf(transactionId)),
                TransactionIndexDTO.class
        );

        return response.found() ? response.source() : null;
    }

    /**
     * Update specific fields of a transaction
     */
    public void updateTransactionFields(
            int userId,
            int transactionId,
            Map<String, Object> partialUpdate
    ) throws IOException {
        String indexName = getIndexName(userId);

        client.update(u -> u
                        .index(indexName)
                        .id(String.valueOf(transactionId))
                        .doc(partialUpdate),
                Void.class
        );
    }

    /**
     * Delete a transaction
     */
    public void deleteTransaction(int userId, int transactionId) throws IOException {
        String indexName = getIndexName(userId);

        client.delete(d -> d
                .index(indexName)
                .id(String.valueOf(transactionId))
        );
    }

    /**
     * Search transactions for a user (basic example)
     */
    public List<TransactionIndexDTO> searchUserTransactions(
            int userId,
            String searchQuery
    ) throws IOException {
        String indexName = getIndexName(userId);

        SearchResponse<TransactionIndexDTO> response = client.search(s -> s
                        .index(indexName)
                        .query(q -> q
                                .multiMatch(m -> m
                                        .query(searchQuery)
                                        .fields("description", "recipientName", "tags")
                                )
                        ),
                TransactionIndexDTO.class
        );

        return response.hits().hits().stream()
                .map(hit -> hit.source())
                .toList();
    }

    // ========== Helper Methods ========== //

    public static String getIndexName(int userId) {
        return SearchConstants.TRANSACTION_INDEX_NAME_PREFIX + userId;
    }

}
