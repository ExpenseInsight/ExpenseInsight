package com.analytics.expenseinsight.service;

import com.analytics.expenseinsight.model.Tag;
import com.analytics.expenseinsight.model.User;
import com.analytics.expenseinsight.repository.TagRepository;
import com.analytics.expenseinsight.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {

    @Autowired
    TagRepository tagRepository;

    @Autowired
    UserRepository userRepository;

    public ResponseEntity<String> createTag(Tag tag) {
        try {
            tagRepository.save(tag);
            return new ResponseEntity<>("Tag created successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error creating tag", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Tag> getTagById(int tagId) {
        Optional<Tag> tag = tagRepository.findById(tagId);
        return tag.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<List<Tag>> getTagsByUserId(int userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(tagRepository.findByUser(user.get()), HttpStatus.OK);
    }

    public ResponseEntity<Tag> updateTag(int tagId, Tag tag) {
        Optional<Tag> existingTagOpt = tagRepository.findById(tagId);
        if (existingTagOpt.isPresent()) {
            Tag existingTag = existingTagOpt.get();
            existingTag.setTagName(tag.getTagName());
            existingTag.setDescription(tag.getDescription());
            existingTag.setParentTag(tag.getParentTag());
            return new ResponseEntity<>(tagRepository.save(existingTag), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<String> deleteTag(int tagId) {
        try {
            tagRepository.deleteById(tagId);
            return new ResponseEntity<>("Tag deleted", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error deleting tag", HttpStatus.BAD_REQUEST);
        }
    }
}
