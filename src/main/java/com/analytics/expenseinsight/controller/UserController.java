package com.analytics.expenseinsight.controller;

import com.analytics.expenseinsight.model.User;
import com.analytics.expenseinsight.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/userinfo")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("create")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("allusers")
    public ResponseEntity<List<User>> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/read/{userId}")
    public ResponseEntity<Optional<User>> getUserById(@PathVariable int userId) {
        return userService.getUserById(userId);
    }

    @PutMapping("/update/{userId}")

    public ResponseEntity<User> updateUserById(@RequestBody User user, @PathVariable int userId) {
        return userService.updateUserById(userId, user);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUserById(@PathVariable int userId) {
        return userService.deleteUserById(userId);
    }

}


