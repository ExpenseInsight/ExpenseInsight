package com.analytics.expenseinsight.restapi.repository;

import com.analytics.expenseinsight.restapi.model.Tag;
import com.analytics.expenseinsight.restapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    List<Tag> findByUser(User user);
}
