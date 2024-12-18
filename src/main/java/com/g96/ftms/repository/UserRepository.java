package com.g96.ftms.repository;

import com.g96.ftms.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
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

    @Query("SELECT u FROM User u JOIN u.userClassRelationList r WHERE r.classs.classId = :classId")
    List<User> findUsersByClassId(@Param("classId") Long classId);

    @Query("SELECT u FROM User u JOIN u.roles r " +
            " WHERE " +
            "(:keywordFilter IS NULL OR (u.account LIKE :keywordFilter OR u.email LIKE :keywordFilter OR u.phone LIKE :keywordFilter)) " +
            "AND (:status IS NULL OR u.status= :status) " +
            "AND (:roleId IS NULL OR r.roleId =:roleId)")
    Page<User> searchFilter(@Param("keywordFilter") String keywordFilter,
                            @Param("status") Boolean status,
                            @Param("roleId") Long roleId,
                            Pageable pageable);

    @Query("SELECT u FROM User u JOIN u.roles r " +
            "WHERE r.roleName = :roleName " +
            "AND (:traineeUnAvailable IS NULL OR u.account NOT IN :traineeUnAvailable)")
    List<User> findByRoleAvail(
            @Param("roleName") String roleName,
            @Param("traineeUnAvailable") List<String> traineeUnAvailable
    );

    @Query("SELECT u FROM User u JOIN u.roles r " +
            "WHERE r.roleName = :roleName ")
    List<User> findByRole(
            @Param("roleName") String roleName
    );

    @Query("SELECT u FROM User u JOIN u.gradeSummaryList g " +
            "WHERE " +
            "g.isPassed=true " +
            "AND g.classs.classId= :classId " +
            "AND (:keywordFilter IS NULL OR (u.account LIKE :keywordFilter OR u.email LIKE :keywordFilter OR u.phone LIKE :keywordFilter)) " +
            "AND (:status IS NULL OR u.status= :status) ")
    Page<User> searchUserPass(@Param("keywordFilter") String keywordFilter,
                              @Param("status") Boolean status,
                              @Param("classId") Long classId,
                              Pageable pageable);

    @Query("SELECT u FROM User u " +
            "LEFT JOIN u.userClassRelationList r " +
            "JOIN u.roles ro " +
            "WHERE r.classs IS NULL " +
            "AND ro.roleName = 'ROLE_TRAINEE'")
    List<User> findUsersNotInAnyClass();
}