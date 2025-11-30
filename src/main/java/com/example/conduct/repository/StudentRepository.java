package com.example.conduct.repository;

import com.example.conduct.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("""
           SELECT s FROM Student s
           WHERE (:status IS NULL OR s.status = :status)
             AND (:classId IS NULL OR s.classEntity.classId = :classId)
             AND (
                  :keyword IS NULL
                  OR LOWER(s.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                  OR LOWER(s.mssv)     LIKE LOWER(CONCAT('%', :keyword, '%'))
             )
           """)
    List<Student> searchStudents(@Param("keyword") String keyword,
                                 @Param("status") Integer status,
                                 @Param("classId") Long classId);

    long countByStatus(Integer status);
}
