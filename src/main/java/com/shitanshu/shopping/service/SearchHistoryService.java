package com.shitanshu.shopping.service;

import com.shitanshu.shopping.model.SearchHistory;
import com.shitanshu.shopping.model.User;
import com.shitanshu.shopping.repository.SearchHistoryRepository;
import com.shitanshu.shopping.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SearchHistoryService {

    private static final int MAX_HISTORY_LIMIT = 5;
    private static final int TOP_DISPLAY_LIMIT = 4;

    @Autowired
    private SearchHistoryRepository searchHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void recordSearch(Integer userId, String rawQuery) {
        if (rawQuery == null || rawQuery.trim().isEmpty()) {
            return;
        }

        String cleanQuery = rawQuery.trim();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Optional<SearchHistory> existing = searchHistoryRepository.findByUserIdAndQueryIgnoreCase(userId, cleanQuery);
        if (existing.isPresent()) {
            SearchHistory history = existing.get();
            history.setSearchedAt(LocalDateTime.now());
            searchHistoryRepository.save(history);
        } else {
            SearchHistory newHistory = new SearchHistory(cleanQuery, LocalDateTime.now(), user);
            searchHistoryRepository.save(newHistory);
        }

        pruneOldSearches(userId);
    }

    @Transactional(readOnly = true)
    public List<String> getTopSearches(Integer userId) {
        List<SearchHistory> topList = searchHistoryRepository.findByUserIdOrderBySearchedAtDesc(
                userId,
                PageRequest.of(0, TOP_DISPLAY_LIMIT)
        );

        return topList.stream()
                .map(SearchHistory::getQuery)
                .collect(Collectors.toList());
    }

    @Transactional
    public void clearHistory(Integer userId) {
        searchHistoryRepository.deleteAllByUserId(userId);
    }

    private void pruneOldSearches(Integer userId) {
        List<SearchHistory> allSearches = searchHistoryRepository.findByUserIdOrderBySearchedAtDesc(userId);

        if (allSearches.size() > MAX_HISTORY_LIMIT) {
            List<Long> idsToDelete = allSearches.subList(MAX_HISTORY_LIMIT, allSearches.size())
                    .stream()
                    .map(SearchHistory::getId)
                    .collect(Collectors.toList());

            if (!idsToDelete.isEmpty()) {
                searchHistoryRepository.deleteByIds(idsToDelete);
            }
        }
    }
}
