package com.shitanshu.shopping.repository;

import com.shitanshu.shopping.model.SearchHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {

    List<SearchHistory> findByUserIdOrderBySearchedAtDesc(Integer userId, Pageable pageable);

    Optional<SearchHistory> findByUserIdAndQueryIgnoreCase(Integer userId, String query);

    List<SearchHistory> findByUserIdOrderBySearchedAtDesc(Integer userId);

    @Modifying
    @Query("DELETE FROM SearchHistory s WHERE s.id IN :ids")
    void deleteByIds(@Param("ids") List<Long> ids);

    @Modifying
    @Query("DELETE FROM SearchHistory s WHERE s.user.id = :userId")
    void deleteAllByUserId(@Param("userId") Integer userId);
}
