package com.analytics.expenseinsight.service;

import com.analytics.expenseinsight.model.User;
import com.analytics.expenseinsight.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public String createUser(User user) {
        userRepository.save(user);
        return "Successfully Created";
    }
}

