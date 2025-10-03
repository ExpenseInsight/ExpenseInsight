package com.analytics.expenseinsight.restapi.repository;

import com.analytics.expenseinsight.restapi.model.Filter;
import com.analytics.expenseinsight.restapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FilterRepository extends JpaRepository<Filter, Integer> {
    List<Filter> findByUser(User user);
}
