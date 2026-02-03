package com.pianoteacher.repository;

import com.pianoteacher.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // Find by name (case-insensitive)
    List<Student> findByNameContainingIgnoreCase(String name);

    // Find by email
    Optional<Student> findByEmail(String email);

    // Find by email (case-insensitive)
    Optional<Student> findByEmailIgnoreCase(String email);

    // Find by age range
    List<Student> findByAgeBetween(Integer minAge, Integer maxAge);

    // Find by grade
    List<Student> findByGrade(String grade);

    // Find by grade (case-insensitive)
    List<Student> findByGradeIgnoreCase(String grade);

    // Find by phone
    Optional<Student> findByPhone(String phone);

    // Custom query to find students with pricing information
    @Query("SELECT s FROM Student s WHERE s.pricing IS NOT NULL")
    List<Student> findStudentsWithPricing();

    // Custom query to find students without pricing information
    @Query("SELECT s FROM Student s WHERE s.pricing IS NULL")
    List<Student> findStudentsWithoutPricing();

    // Search students by name or email
    @Query("SELECT s FROM Student s WHERE " +
           "LOWER(s.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(s.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Student> searchStudents(@Param("searchTerm") String searchTerm);

    // Count students by grade
    @Query("SELECT s.grade, COUNT(s) FROM Student s WHERE s.grade IS NOT NULL GROUP BY s.grade")
    List<Object[]> countStudentsByGrade();

    // Find students created within a date range
    @Query("SELECT s FROM Student s WHERE s.createdAt BETWEEN :startDate AND :endDate")
    List<Student> findStudentsByDateRange(@Param("startDate") java.time.LocalDateTime startDate,
                                          @Param("endDate") java.time.LocalDateTime endDate);

    // Check if email exists (excluding current student)
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Student s WHERE s.email = :email AND s.id != :studentId")
    boolean existsByEmailAndIdNot(@Param("email") String email, @Param("studentId") Long studentId);

    // Check if phone exists (excluding current student)
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Student s WHERE s.phone = :phone AND s.id != :studentId")
    boolean existsByPhoneAndIdNot(@Param("phone") String phone, @Param("studentId") Long studentId);
}
