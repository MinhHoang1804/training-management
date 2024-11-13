package com.g96.ftms.repository;

import com.g96.ftms.entity.Subject;
import com.g96.ftms.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByAccount(String account);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByAccount(String account);

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u JOIN u.userClassRelationList r WHERE r.classs.classId = :classId")
    Page<User> findUsersByClassId(@Param("classId") Long classId, Pageable pageable);

    @Query("SELECT u FROM User u WHERE " +
            "(:keywordFilter IS NULL OR (u.account LIKE :keywordFilter OR u.email LIKE :keywordFilter OR u.phone LIKE :keywordFilter)) " +
            "AND (:status IS NULL OR u.status= :status) ")
    Page<User> searchFilter(@Param("keywordFilter") String keywordFilter,
                               @Param("status") Boolean status,
                               Pageable pageable);
}