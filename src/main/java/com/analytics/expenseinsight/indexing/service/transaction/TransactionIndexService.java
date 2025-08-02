package com.analytics.expenseinsight.indexing.service.transaction;

import com.analytics.expenseinsight.indexing.helper.SearchConstants;
import com.analytics.expenseinsight.indexing.model.TransactionIndexDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransactionIndexService {
    private final RestHighLevelClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    private boolean isIndexExist(String stIndexName) throws IOException {
        return client.indices().exists(new GetIndexRequest(stIndexName), RequestOptions.DEFAULT);

    }

    public void addTransaction(TransactionIndexDTO dto) throws IOException {
        String stIndexName = SearchConstants.TRANSACTION_INDEX_NAME_PREFIX + dto.getId();

        // Check and create index if missing
        if (!isIndexExist(stIndexName)) {
            client.indices().create(new CreateIndexRequest(stIndexName), RequestOptions.DEFAULT);
        }

        Map<String, Object> docMap = mapper.convertValue(dto, Map.class);

        IndexRequest indexRequest = new IndexRequest(stIndexName)
                .id(String.valueOf(dto.getId()))
                .source(docMap);

        client.index(indexRequest, RequestOptions.DEFAULT);
    }

    // READ (Get by ID)
    public Map<String, Object> getTransactionById(String indexName, String id) throws IOException {
        GetRequest getRequest = new GetRequest(indexName, id);
        GetResponse response = client.get(getRequest, RequestOptions.DEFAULT);
        return response.isExists() ? response.getSourceAsMap() : null;
    }

    // UPDATE (Same as create with existing ID)
    public void updateTransaction(String indexName, String id, Map<String, Object> updatedFields) throws IOException {
        UpdateRequest updateRequest = new UpdateRequest(indexName, id).doc(updatedFields);
        client.update(updateRequest, RequestOptions.DEFAULT);
    }

    // DELETE
    public void deleteTransaction(String indexName, String id) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(indexName, id);
        client.delete(deleteRequest, RequestOptions.DEFAULT);
    }

}
