package com.analytics.expenseinsight.restapi.search;

import com.analytics.expenseinsight.indexing.model.TransactionIndexDTO;
import com.analytics.expenseinsight.indexing.service.transaction.TransactionIndexService;
import com.analytics.expenseinsight.indexing.service.transaction.TransactionSearchService;
import com.analytics.expenseinsight.restapi.controller.FilterController;
import com.analytics.expenseinsight.restapi.model.Filter;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/search")
public class FilterSearch {

    @Autowired
    private FilterController filterController;
    @Autowired
    private TransactionSearchService transactionSearchService;

    public String getFormatedDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    @GetMapping("searchfilter/{id}")
    public ResponseEntity searchFilter(@PathVariable int id) throws IOException {

        ResponseEntity<Filter> response = filterController.getFilterById(id);
        if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }

        Filter filter = response.getBody();

        int userId = filter.getUser().getUserId();
        String indexName = TransactionIndexService.getIndexNameFromId(userId);

        List<String> tagList = TransactionIndexDTO.getTagsList(filter.getIncludeTags());

        SearchResponse searchResponse = transactionSearchService.combinedSearch(indexName,tagList,
                filter.getPaymentType(),getFormatedDate(filter.getStartDate()),getFormatedDate(filter.getEndDate()),filter.getMinAmount(),filter.getMaxAmount());
        return new ResponseEntity<SearchResponse>(searchResponse,HttpStatus.OK);
    }
}
