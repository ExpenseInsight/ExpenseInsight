package com.analytics.expenseinsight.indexing.service.transaction;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import co.elastic.clients.transport.endpoints.BooleanResponse;

import com.analytics.expenseinsight.indexing.helper.SearchConstants;
import com.analytics.expenseinsight.indexing.model.TransactionIndexDTO;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class TransactionIndexService {

    @Autowired
    private final ElasticsearchClient client;

    private boolean isIndexExist(String indexName) throws IOException {
        BooleanResponse exists = client.indices().exists(ExistsRequest.of(e -> e.index(indexName)));
        return exists.value();
    }

    public void addTransaction(TransactionIndexDTO dto) {
        String indexName = SearchConstants.TRANSACTION_INDEX_NAME_PREFIX + dto.getUserId();

        try {
            // Create index if not exists
            if (!isIndexExist(indexName)) {
                client.indices().create(CreateIndexRequest.of(c -> c.index(indexName)));
            }

            client.index(IndexRequest.of(i -> i
                    .index(indexName)
                    .id(String.valueOf(dto.getId()))
                    .document(dto)
            ));
        } catch (ElasticsearchException | IOException e) {
            throw new RuntimeException("Unable to add transaction to index: " + e.getMessage(), e);
        }
    }

    // READ (Get by ID)
    public TransactionIndexDTO getTransactionById(String indexName, String id) {
        try {
            GetResponse<TransactionIndexDTO> response = client.get(g -> g
                    .index(indexName)
                    .id(id), TransactionIndexDTO.class);

            return response.found() ? response.source() : null;
        } catch (ElasticsearchException | IOException e) {
            throw new RuntimeException("Unable to fetch transaction: " + e.getMessage(), e);
        }
    }

    // UPDATE
    public void updateTransaction(String indexName, String id, TransactionIndexDTO updatedDto) {
        try {
            client.update(u -> u
                            .index(indexName)
                            .id(id)
                            .doc(updatedDto),
                    TransactionIndexDTO.class);
        } catch (ElasticsearchException | IOException e) {
            throw new RuntimeException("Unable to update transaction: " + e.getMessage(), e);
        }
    }

    // DELETE
    public void deleteTransaction(String indexName, String id) {
        try {
            client.delete(DeleteRequest.of(d -> d
                    .index(indexName)
                    .id(id)
            ));
        } catch (ElasticsearchException | IOException e) {
            throw new RuntimeException("Unable to delete transaction: " + e.getMessage(), e);
        }
    }

    public static String getIndexNameFromId(int userId) {
        return SearchConstants.TRANSACTION_INDEX_NAME_PREFIX + userId;
    }
}
