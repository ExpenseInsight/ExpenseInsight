package com.analytics.expenseinsight.restapi.controller;

import com.analytics.expenseinsight.restapi.model.Filter;
import com.analytics.expenseinsight.restapi.service.FilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/filter")
public class FilterController {

    @Autowired
    private FilterService filterService;

    @PostMapping("/create")
    public ResponseEntity<String> createFilter(@RequestBody Filter filter) {
        return filterService.createFilter(filter);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Filter> getFilterById(@PathVariable int id) {
        return filterService.getFilterById(id);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Filter>> getFiltersByUserId(@PathVariable int userId) {
        return filterService.getFiltersByUserId(userId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Filter> updateFilter(@PathVariable int id, @RequestBody Filter filter) {
        return filterService.updateFilterById(id, filter);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFilter(@PathVariable int id) {
        return filterService.deleteFilter(id);
    }
}
