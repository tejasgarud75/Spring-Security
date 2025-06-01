package com.example.repository;

import com.example.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUserName(String username);

  boolean existsByUserNameOrEmail(String userName, String email);

}
