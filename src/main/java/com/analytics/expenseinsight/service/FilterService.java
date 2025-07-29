package com.analytics.expenseinsight.service;

import com.analytics.expenseinsight.model.Filter;
import com.analytics.expenseinsight.model.User;
import com.analytics.expenseinsight.repository.FilterRepository;
import com.analytics.expenseinsight.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FilterService {

    @Autowired
    private FilterRepository filterRepository;

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<String> createFilter(Filter filter) {
        try {
            filter.setCreatedAt(LocalDateTime.now());
            filter.setUpdatedAt(LocalDateTime.now());
            filterRepository.save(filter);
            return new ResponseEntity<>("Filter saved", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to create filter", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Filter> getFilterById(int filterId) {
        Optional<Filter> filter = filterRepository.findById(filterId);
        return filter.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<List<Filter>> getFiltersByUserId(int userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(filterRepository.findByUser(user.get()), HttpStatus.OK);
    }

    public ResponseEntity<Filter> updateFilterById(int filterId, Filter newFilter) {
        Optional<Filter> optionalFilter = filterRepository.findById(filterId);
        if (optionalFilter.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Filter existingFilter = optionalFilter.get();
        existingFilter.setFilterName(newFilter.getFilterName());
        existingFilter.setStartDate(newFilter.getStartDate());
        existingFilter.setEndDate(newFilter.getEndDate());
        existingFilter.setIncludeTags(newFilter.getIncludeTags());
        existingFilter.setExcludeTags(newFilter.getExcludeTags());
        existingFilter.setUpdatedAt(LocalDateTime.now());

        Filter saved = filterRepository.save(existingFilter);
        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    public ResponseEntity<String> deleteFilter(int filterId) {
        try {
            filterRepository.deleteById(filterId);
            return new ResponseEntity<>("Filter deleted", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Deletion failed", HttpStatus.NOT_FOUND);
        }
    }
}
