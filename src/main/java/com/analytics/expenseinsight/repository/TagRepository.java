package com.analytics.expenseinsight.repository;

import com.analytics.expenseinsight.model.Tag;
import com.analytics.expenseinsight.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    List<Tag> findByUser(User user);
}
