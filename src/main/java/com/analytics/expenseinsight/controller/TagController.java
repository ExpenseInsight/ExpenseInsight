package com.analytics.expenseinsight.controller;

import com.analytics.expenseinsight.model.Tag;
import com.analytics.expenseinsight.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/taginfo")
public class TagController {

    @Autowired
    TagService tagService;

    @PostMapping("/create")
    public ResponseEntity<String> createTag(@RequestBody Tag tag) {
        return tagService.createTag(tag);
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<Tag> getTagById(@PathVariable int id) {
        return tagService.getTagById(id);
    }

    @GetMapping("/alltags/{id}")
    public ResponseEntity<List<Tag>> getTagsByUserId(@PathVariable int id) {
        return tagService.getTagsByUserId(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Tag> updateTag(@PathVariable int id, @RequestBody Tag tag) {
        return tagService.updateTag(id, tag);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTag(@PathVariable int id) {
        return tagService.deleteTag(id);
    }
}
