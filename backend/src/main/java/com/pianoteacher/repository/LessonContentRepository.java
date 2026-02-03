package com.pianoteacher.repository;

import com.pianoteacher.model.LessonContent;
import com.pianoteacher.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LessonContentRepository extends JpaRepository<LessonContent, Long> {

    // Find by student
    List<LessonContent> findByStudent(Student student);
    List<LessonContent> findByStudentId(Long studentId);

    // Find by content type
    List<LessonContent> findByContentType(String contentType);

    // Find by difficulty level
    List<LessonContent> findByDifficultyLevel(Integer difficultyLevel);

    // Find by completion status
    List<LessonContent> findByCompleted(Boolean completed);

    // Find by student and content type
    List<LessonContent> findByStudentIdAndContentType(Long studentId, String contentType);

    // Find by student and difficulty level
    List<LessonContent> findByStudentIdAndDifficultyLevel(Long studentId, Integer difficultyLevel);

    // Find by student and completion status
    List<LessonContent> findByStudentIdAndCompleted(Long studentId, Boolean completed);

    // Find by difficulty range
    @Query("SELECT lc FROM LessonContent lc WHERE lc.difficultyLevel BETWEEN :minLevel AND :maxLevel")
    List<LessonContent> findByDifficultyRange(@Param("minLevel") Integer minLevel, @Param("maxLevel") Integer maxLevel);

    // Find by student and difficulty range
    @Query("SELECT lc FROM LessonContent lc WHERE lc.student.id = :studentId AND lc.difficultyLevel BETWEEN :minLevel AND :maxLevel")
    List<LessonContent> findByStudentIdAndDifficultyRange(@Param("studentId") Long studentId, 
                                                            @Param("minLevel") Integer minLevel, 
                                                            @Param("maxLevel") Integer maxLevel);

    // Find by estimated duration range
    @Query("SELECT lc FROM LessonContent lc WHERE lc.estimatedDuration BETWEEN :minDuration AND :maxDuration")
    List<LessonContent> findByDurationRange(@Param("minDuration") Integer minDuration, @Param("maxDuration") Integer maxDuration);

    // Find by student and estimated duration range
    @Query("SELECT lc FROM LessonContent lc WHERE lc.student.id = :studentId AND lc.estimatedDuration BETWEEN :minDuration AND :maxDuration")
    List<LessonContent> findByStudentIdAndDurationRange(@Param("studentId") Long studentId,
                                                          @Param("minDuration") Integer minDuration, 
                                                          @Param("maxDuration") Integer maxDuration);

    // Find by completion date range
    @Query("SELECT lc FROM LessonContent lc WHERE lc.completionDate BETWEEN :startDate AND :endDate")
    List<LessonContent> findByCompletionDateRange(@Param("startDate") LocalDateTime startDate, 
                                                   @Param("endDate") LocalDateTime endDate);

    // Find by student and completion date range
    @Query("SELECT lc FROM LessonContent lc WHERE lc.student.id = :studentId AND lc.completionDate BETWEEN :startDate AND :endDate")
    List<LessonContent> findByStudentIdAndCompletionDateRange(@Param("studentId") Long studentId,
                                                               @Param("startDate") LocalDateTime startDate,
                                                               @Param("endDate") LocalDateTime endDate);

    // Search by title or description
    @Query("SELECT lc FROM LessonContent lc WHERE lc.title LIKE %:searchTerm% OR lc.description LIKE %:searchTerm%")
    List<LessonContent> searchByTitleOrDescription(@Param("searchTerm") String searchTerm);

    // Search by student and title or description
    @Query("SELECT lc FROM LessonContent lc WHERE lc.student.id = :studentId AND (lc.title LIKE %:searchTerm% OR lc.description LIKE %:searchTerm%)")
    List<LessonContent> searchByStudentAndTitleOrDescription(@Param("studentId") Long studentId,
                                                               @Param("searchTerm") String searchTerm);

    // Count by content type
    @Query("SELECT lc.contentType, COUNT(lc) FROM LessonContent lc GROUP BY lc.contentType")
    List<Object[]> countByContentType();

    // Count by difficulty level
    @Query("SELECT lc.difficultyLevel, COUNT(lc) FROM LessonContent lc GROUP BY lc.difficultyLevel ORDER BY lc.difficultyLevel")
    List<Object[]> countByDifficultyLevel();

    // Count by student
    @Query("SELECT lc.student.id, COUNT(lc) FROM LessonContent lc GROUP BY lc.student.id")
    List<Object[]> countByStudent();

    // Count completed vs incomplete
    @Query("SELECT lc.completed, COUNT(lc) FROM LessonContent lc GROUP BY lc.completed")
    List<Object[]> countByCompletionStatus();

    // Find uncompleted content for a student
    @Query("SELECT lc FROM LessonContent lc WHERE lc.student.id = :studentId AND lc.completed = false ORDER BY lc.difficultyLevel")
    List<LessonContent> findUncompletedByStudent(@Param("studentId") Long studentId);

    // Find recently completed content
    @Query("SELECT lc FROM LessonContent lc WHERE lc.completed = true ORDER BY lc.completionDate DESC")
    List<LessonContent> findRecentlyCompleted();

    // Find recently completed content by student
    @Query("SELECT lc FROM LessonContent lc WHERE lc.student.id = :studentId AND lc.completed = true ORDER BY lc.completionDate DESC")
    List<LessonContent> findRecentlyCompletedByStudent(@Param("studentId") Long studentId);

    // Find content created within date range
    @Query("SELECT lc FROM LessonContent lc WHERE lc.createdAt BETWEEN :startDate AND :endDate")
    List<LessonContent> findByCreationDateRange(@Param("startDate") LocalDateTime startDate,
                                                   @Param("endDate") LocalDateTime endDate);

    // Find content by student created within date range
    @Query("SELECT lc FROM LessonContent lc WHERE lc.student.id = :studentId AND lc.createdAt BETWEEN :startDate AND :endDate")
    List<LessonContent> findByStudentIdAndCreationDateRange(@Param("studentId") Long studentId,
                                                             @Param("startDate") LocalDateTime startDate,
                                                             @Param("endDate") LocalDateTime endDate);

    // Get completion statistics for a student
    @Query("SELECT COUNT(lc), COUNT(CASE WHEN lc.completed = true THEN 1 END) FROM LessonContent lc WHERE lc.student.id = :studentId")
    Object[] getStudentCompletionStats(@Param("studentId") Long studentId);

    // Get overall completion statistics
    @Query("SELECT COUNT(lc), COUNT(CASE WHEN lc.completed = true THEN 1 END) FROM LessonContent lc")
    Object[] getOverallCompletionStats();
}
