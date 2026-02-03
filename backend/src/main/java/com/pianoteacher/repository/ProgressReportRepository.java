package com.pianoteacher.repository;

import com.pianoteacher.model.ProgressReport;
import com.pianoteacher.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProgressReportRepository extends JpaRepository<ProgressReport, Long> {

    // Find by student
    List<ProgressReport> findByStudent(Student student);
    List<ProgressReport> findByStudentId(Long studentId);

    // Find by report type
    List<ProgressReport> findByReportType(String reportType);

    // Find by report period
    List<ProgressReport> findByReportPeriod(String reportPeriod);

    // Find by student and report type
    List<ProgressReport> findByStudentIdAndReportType(Long studentId, String reportType);

    // Find by student and report period
    List<ProgressReport> findByStudentIdAndReportPeriod(Long studentId, String reportPeriod);

    // Find by student, report type, and period
    List<ProgressReport> findByStudentIdAndReportTypeAndReportPeriod(Long studentId, String reportType, String reportPeriod);

    // Find by report date range
    @Query("SELECT pr FROM ProgressReport pr WHERE pr.reportDate BETWEEN :startDate AND :endDate")
    List<ProgressReport> findByReportDateRange(@Param("startDate") LocalDateTime startDate, 
                                                 @Param("endDate") LocalDateTime endDate);

    // Find by student and report date range
    @Query("SELECT pr FROM ProgressReport pr WHERE pr.student.id = :studentId AND pr.reportDate BETWEEN :startDate AND :endDate")
    List<ProgressReport> findByStudentIdAndReportDateRange(@Param("studentId") Long studentId,
                                                           @Param("startDate") LocalDateTime startDate,
                                                           @Param("endDate") LocalDateTime endDate);

    // Find by overall progress range
    @Query("SELECT pr FROM ProgressReport pr WHERE pr.overallProgress BETWEEN :minProgress AND :maxProgress")
    List<ProgressReport> findByOverallProgressRange(@Param("minProgress") Double minProgress, 
                                                     @Param("maxProgress") Double maxProgress);

    // Find by student and overall progress range
    @Query("SELECT pr FROM ProgressReport pr WHERE pr.student.id = :studentId AND pr.overallProgress BETWEEN :minProgress AND :maxProgress")
    List<ProgressReport> findByStudentIdAndOverallProgressRange(@Param("studentId") Long studentId,
                                                                 @Param("minProgress") Double minProgress,
                                                                 @Param("maxProgress") Double maxProgress);

    // Find latest report for each student
    @Query("SELECT pr FROM ProgressReport pr WHERE pr.id IN " +
           "(SELECT MAX(pr2.id) FROM ProgressReport pr2 WHERE pr2.student.id = pr.student.id)")
    List<ProgressReport> findLatestReportsForAllStudents();

    // Find latest report for specific student
    @Query("SELECT pr FROM ProgressReport pr WHERE pr.student.id = :studentId ORDER BY pr.reportDate DESC")
    List<ProgressReport> findLatestReportsByStudent(@Param("studentId") Long studentId);

    // Find latest report for specific student by type
    @Query("SELECT pr FROM ProgressReport pr WHERE pr.student.id = :studentId AND pr.reportType = :reportType ORDER BY pr.reportDate DESC")
    List<ProgressReport> findLatestReportsByStudentAndType(@Param("studentId") Long studentId, @Param("reportType") String reportType);

    // Count reports by student
    @Query("SELECT pr.student.id, COUNT(pr) FROM ProgressReport pr GROUP BY pr.student.id")
    List<Object[]> countReportsByStudent();

    // Count reports by report type
    @Query("SELECT pr.reportType, COUNT(pr) FROM ProgressReport pr GROUP BY pr.reportType")
    List<Object[]> countReportsByType();

    // Count reports by period
    @Query("SELECT pr.reportPeriod, COUNT(pr) FROM ProgressReport pr GROUP BY pr.reportPeriod ORDER BY pr.reportPeriod")
    List<Object[]> countReportsByPeriod();

    // Get average progress by student
    @Query("SELECT pr.student.id, AVG(pr.overallProgress) FROM ProgressReport pr GROUP BY pr.student.id")
    List<Object[]> getAverageProgressByStudent();

    // Get average progress by report type
    @Query("SELECT pr.reportType, AVG(pr.overallProgress) FROM ProgressReport pr GROUP BY pr.reportType")
    List<Object[]> getAverageProgressByReportType();

    // Get progress trend for student
    @Query("SELECT pr.reportDate, pr.overallProgress FROM ProgressReport pr WHERE pr.student.id = :studentId ORDER BY pr.reportDate")
    List<Object[]> getProgressTrendForStudent(@Param("studentId") Long studentId);

    // Get progress trend for all students
    @Query("SELECT pr.reportDate, AVG(pr.overallProgress) FROM ProgressReport pr GROUP BY pr.reportDate ORDER BY pr.reportDate")
    List<Object[]> getOverallProgressTrend();

    // Find reports with high performance
    @Query("SELECT pr FROM ProgressReport pr WHERE pr.overallProgress >= :threshold")
    List<ProgressReport> findHighPerformingReports(@Param("threshold") Double threshold);

    // Find reports with low performance
    @Query("SELECT pr FROM ProgressReport pr WHERE pr.overallProgress <= :threshold")
    List<ProgressReport> findLowPerformingReports(@Param("threshold") Double threshold);

    // Find reports with high performance by student
    @Query("SELECT pr FROM ProgressReport pr WHERE pr.student.id = :studentId AND pr.overallProgress >= :threshold")
    List<ProgressReport> findHighPerformingReportsByStudent(@Param("studentId") Long studentId, @Param("threshold") Double threshold);

    // Find reports with low performance by student
    @Query("SELECT pr FROM ProgressReport pr WHERE pr.student.id = :studentId AND pr.overallProgress <= :threshold")
    List<ProgressReport> findLowPerformingReportsByStudent(@Param("studentId") Long studentId, @Param("threshold") Double threshold);

    // Get skill averages across all reports
    @Query("SELECT AVG(pr.technicalSkills), AVG(pr.theoryKnowledge), AVG(pr.repertoireSkills), AVG(pr.practiceHabits) FROM ProgressReport pr WHERE pr.technicalSkills IS NOT NULL AND pr.theoryKnowledge IS NOT NULL AND pr.repertoireSkills IS NOT NULL AND pr.practiceHabits IS NOT NULL")
    Object[] getOverallSkillAverages();

    // Get skill averages for specific student
    @Query("SELECT AVG(pr.technicalSkills), AVG(pr.theoryKnowledge), AVG(pr.repertoireSkills), AVG(pr.practiceHabits) FROM ProgressReport pr WHERE pr.student.id = :studentId AND pr.technicalSkills IS NOT NULL AND pr.theoryKnowledge IS NOT NULL AND pr.repertoireSkills IS NOT NULL AND pr.practiceHabits IS NOT NULL")
    Object[] getSkillAveragesForStudent(@Param("studentId") Long studentId);

    // Find reports created within date range
    @Query("SELECT pr FROM ProgressReport pr WHERE pr.createdAt BETWEEN :startDate AND :endDate")
    List<ProgressReport> findByCreationDateRange(@Param("startDate") LocalDateTime startDate,
                                                 @Param("endDate") LocalDateTime endDate);

    // Find reports by student created within date range
    @Query("SELECT pr FROM ProgressReport pr WHERE pr.student.id = :studentId AND pr.createdAt BETWEEN :startDate AND :endDate")
    List<ProgressReport> findByStudentIdAndCreationDateRange(@Param("studentId") Long studentId,
                                                             @Param("startDate") LocalDateTime startDate,
                                                             @Param("endDate") LocalDateTime endDate);

    // Get performance statistics
    @Query("SELECT COUNT(pr), AVG(pr.overallProgress), MIN(pr.overallProgress), MAX(pr.overallProgress) FROM ProgressReport pr")
    Object[] getOverallPerformanceStatistics();

    // Get performance statistics for student
    @Query("SELECT COUNT(pr), AVG(pr.overallProgress), MIN(pr.overallProgress), MAX(pr.overallProgress) FROM ProgressReport pr WHERE pr.student.id = :studentId")
    Object[] getPerformanceStatisticsForStudent(@Param("studentId") Long studentId);

    // Find most recent report for each student by type
    @Query("SELECT pr FROM ProgressReport pr WHERE pr.id IN " +
           "(SELECT MAX(pr2.id) FROM ProgressReport pr2 WHERE pr2.student.id = pr.student.id AND pr2.reportType = pr.reportType)")
    List<ProgressReport> findMostRecentReportsByType();

    // Check if report exists for student, type, and period
    @Query("SELECT CASE WHEN COUNT(pr) > 0 THEN true ELSE false END FROM ProgressReport pr WHERE pr.student.id = :studentId AND pr.reportType = :reportType AND pr.reportPeriod = :reportPeriod")
    boolean existsByStudentAndTypeAndPeriod(@Param("studentId") Long studentId, @Param("reportType") String reportType, @Param("reportPeriod") String reportPeriod);
}
