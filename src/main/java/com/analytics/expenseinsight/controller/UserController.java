package com.analytics.expenseinsight.controller;

import com.analytics.expenseinsight.model.User;
import com.analytics.expenseinsight.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userinfo")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("create")
    public String createUser(@RequestBody User user){
        return userService.createUser(user);
    }

    @GetMapping("test")
    public String testApi(){
        System.out.println("Hi i am good!!");
        return "I am Working Fine";
    }
}


