
package com.shitanshu.shopping.repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.shitanshu.shopping.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	 boolean existsByEmail(String email);
	 Optional<User> findByEmail(String email);
}