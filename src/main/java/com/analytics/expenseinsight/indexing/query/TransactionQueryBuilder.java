package com.analytics.expenseinsight.indexing.query;

import org.apache.lucene.queryparser.flexible.core.builders.QueryBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Component;

@Component
public class TransactionQueryBuilder {

    public QueryBuilder buildTagAndDateQuery(String tag, String startDate, String endDate) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        if (tag != null) {
            boolQuery.must(QueryBuilders.matchQuery("tag", tag));
        }

        if (startDate != null && endDate != null) {
            boolQuery.filter(QueryBuilders.rangeQuery("date")
                    .gte(startDate)
                    .lte(endDate));
        }

        return (QueryBuilder) boolQuery;
    }
}

