package com.g96.ftms.repository;

import com.g96.ftms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByAccount(String account);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByAccount(String account);

    Optional<User> findByEmail(String email);
}