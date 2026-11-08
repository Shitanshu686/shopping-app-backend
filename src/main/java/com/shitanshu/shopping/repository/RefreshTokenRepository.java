package com.shitanshu.shopping.repository;

import com.shitanshu.shopping.model.RefreshToken;
import com.shitanshu.shopping.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser(User user);

    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.data.jpa.repository.Query("DELETE FROM RefreshToken r WHERE r.user = :user")
    void deleteByUser(@org.springframework.data.repository.query.Param("user") com.shitanshu.shopping.model.User user);

}
