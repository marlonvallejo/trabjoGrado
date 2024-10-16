package com.hotelandinocode.hotelandino.repositories;

import com.hotelandinocode.hotelandino.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);
    List<User> findAll();

    Optional<User> findById(Long id);

    boolean existsById(Long id);

}
