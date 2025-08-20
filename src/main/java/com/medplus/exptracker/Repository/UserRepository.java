package com.medplus.exptracker.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.medplus.exptracker.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}
