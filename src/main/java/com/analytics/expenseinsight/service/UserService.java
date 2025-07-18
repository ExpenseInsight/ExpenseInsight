package com.analytics.expenseinsight.service;

import com.analytics.expenseinsight.model.User;
import com.analytics.expenseinsight.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public ResponseEntity<String> createUser(User user) {
        try {
            userRepository.save(user);
            return new ResponseEntity<>("Successfully Created", HttpStatus.CREATED);
          } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<User>> getAllUsers() {
        try {
            return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);

    }
    public ResponseEntity<Optional<User>> getUserById(int userId) {
        try {
            return new ResponseEntity<>(userRepository.findById(userId), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }
    public ResponseEntity<User> updateUserById(int userId, User user) {
            try{
                Optional<User> u=userRepository.findById(userId);
                if(u.isPresent()){
                    User existingUser=u.get();

                    existingUser.setUserName(user.getUserName());
                    existingUser.setEmail(user.getEmail());
                    existingUser.setPasswordHash(user.getPasswordHash());
                    existingUser.setPhoneNumber(user.getPhoneNumber());
                    existingUser.setCreatedAt(user.getCreatedAt());
                    existingUser.setUpdatedAt(user.getUpdatedAt());
                 User savedUser=userRepository.save(existingUser);
                 return new ResponseEntity<>(savedUser,HttpStatus.OK);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
       return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<String> deleteUserById(int userId) {
       try{
           userRepository.deleteById(userId);
           return new ResponseEntity<>("Successfully Deleted",HttpStatus.OK);
       } catch (Exception e) {
           e.printStackTrace();
       }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }
}

