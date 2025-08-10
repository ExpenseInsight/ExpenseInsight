package com.analytics.expenseinsight.indexing.source.transaction;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransactionSourceBuilder {

    /**
     * Builds a paginated and sorted SearchSourceBuilder for Elasticsearch.
     *
     * @param query        The query builder.
     * @param size         Number of results to return.
     * @param searchAfter  The `search_after` values for pagination.
     * @param sortFields   List of field names to sort by.
     * @param sortOrders   Corresponding sort orders (must match size of sortFields).
     * @return Configured SearchSourceBuilder.
     */
    public SearchSourceBuilder buildPaginatedSortedSource (
            QueryBuilder query,
            int size,
            Object[] searchAfter,
            List<String> sortFields,
            List<SortOrder> sortOrders
    ) {
        SearchSourceBuilder builder = new SearchSourceBuilder()
                .query(query)
                .size(size);

        for (int i = 0; i < sortFields.size(); i++) {
            builder.sort(new FieldSortBuilder(sortFields.get(i)).order(sortOrders.get(i)));
        }

        if (searchAfter != null) {
            builder.searchAfter(searchAfter);
        }

        return builder;
    }

    public SearchSourceBuilder buildSource(QueryBuilder query) {
        return new SearchSourceBuilder().query(query);
    }

}
