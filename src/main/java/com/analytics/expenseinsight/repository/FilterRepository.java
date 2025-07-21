package com.analytics.expenseinsight.repository;

import com.analytics.expenseinsight.model.Filter;
import com.analytics.expenseinsight.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FilterRepository extends JpaRepository<Filter, Integer> {
    List<Filter> findByUser(User user);
}
