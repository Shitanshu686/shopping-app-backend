package com.shitanshu.shopping.controller;

import com.shitanshu.shopping.service.SearchHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/search-history")
@CrossOrigin(origins = "*")
public class SearchHistoryController {

    @Autowired
    private SearchHistoryService searchHistoryService;

    @PostMapping
    public ResponseEntity<?> addSearch(@RequestParam Integer userId, @RequestParam String query) {
        searchHistoryService.recordSearch(userId, query);
        return ResponseEntity.ok(Map.of("message", "Search recorded successfully"));
    }

    @GetMapping
    public ResponseEntity<List<String>> getTopSearches(@RequestParam Integer userId) {
        List<String> topSearches = searchHistoryService.getTopSearches(userId);
        return ResponseEntity.ok(topSearches);
    }

    @DeleteMapping
    public ResponseEntity<?> clearHistory(@RequestParam Integer userId) {
        searchHistoryService.clearHistory(userId);
        return ResponseEntity.ok(Map.of("message", "Search history cleared"));
    }
}
