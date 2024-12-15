package com.g96.ftms.repository;

import com.g96.ftms.entity.Class;
import com.g96.ftms.entity.Curriculum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClassRepository extends JpaRepository<Class, Long> {
    @Query("SELECT c FROM Class c WHERE " +
            "(:keywordFilter IS NULL OR (c.classCode LIKE :keywordFilter OR c.descriptions LIKE :keywordFilter)) " +
            "AND (:status IS NULL OR c.status= :status) ")
    Page<Class> searchFilter(@Param("keywordFilter") String keywordFilter,
                             @Param("status") Boolean status,
                             Pageable pageable);

    @Query("SELECT c FROM Class c WHERE c.endDate <= :now AND c.status = true")
    List<Class> findCloseClass(@Param("now") LocalDateTime now);

    @Query("SELECT c FROM Class c WHERE " +
            "(:keywordFilter IS NULL OR (c.classCode LIKE :keywordFilter OR c.descriptions LIKE :keywordFilter)) " +
            "AND (:status IS NULL OR c.status= :status) " +
            "AND c.admin= :username")
    Page<Class> searchFilterByAdmin(@Param("keywordFilter") String keywordFilter,
                                    @Param("status") Boolean status,
                                    @Param("username") String username,
                                    Pageable pageable);

    @Query("SELECT c FROM Class c WHERE " +
            "(:keywordFilter IS NULL OR (c.classCode LIKE :keywordFilter OR c.descriptions LIKE :keywordFilter)) " +
            "AND (:status IS NULL OR c.status= :status) " +
            "AND c.manager= :username")
    Page<Class> searchFilterByManager(@Param("keywordFilter") String keywordFilter,
                                      @Param("status") Boolean status,
                                      @Param("username") String username,
                                      Pageable pageable);

    @Query("SELECT count(c)>0 FROM Class c WHERE c.endDate <= :now " +
            "AND c.classId =:classId " +
            "AND c.status = true")
    Boolean checkClassInTime(@Param("classId") Long classId,@Param("now") LocalDateTime now);
}
