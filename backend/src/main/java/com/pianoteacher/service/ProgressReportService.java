package com.pianoteacher.service;

import com.pianoteacher.model.ProgressReport;
import com.pianoteacher.model.Student;
import com.pianoteacher.repository.ProgressReportRepository;
import com.pianoteacher.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProgressReportService {

    private final ProgressReportRepository progressReportRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public ProgressReportService(ProgressReportRepository progressReportRepository, StudentRepository studentRepository) {
        this.progressReportRepository = progressReportRepository;
        this.studentRepository = studentRepository;
    }

    // ProgressReport CRUD operations
    public ProgressReport createProgressReport(ProgressReport progressReport) {
        progressReport.validate();
        
        // Check if student exists
        if (!studentRepository.existsById(progressReport.getStudent().getId())) {
            throw new IllegalArgumentException("Student not found with id: " + progressReport.getStudent().getId());
        }
        
        // Set report date if not provided
        if (progressReport.getReportDate() == null) {
            progressReport.setReportDate(LocalDateTime.now());
        }
        
        return progressReportRepository.save(progressReport);
    }

    public ProgressReport updateProgressReport(Long id, ProgressReport progressReportDetails) {
        ProgressReport existingReport = getProgressReportById(id);
        
        // Check if student exists
        if (!studentRepository.existsById(progressReportDetails.getStudent().getId())) {
            throw new IllegalArgumentException("Student not found with id: " + progressReportDetails.getStudent().getId());
        }
        
        // Update report details
        existingReport.setStudent(progressReportDetails.getStudent());
        existingReport.setReportType(progressReportDetails.getReportType());
        existingReport.setReportPeriod(progressReportDetails.getReportPeriod());
        existingReport.setOverallProgress(progressReportDetails.getOverallProgress());
        existingReport.setTechnicalSkills(progressReportDetails.getTechnicalSkills());
        existingReport.setTheoryKnowledge(progressReportDetails.getTheoryKnowledge());
        existingReport.setRepertoireSkills(progressReportDetails.getRepertoireSkills());
        existingReport.setPracticeHabits(progressReportDetails.getPracticeHabits());
        existingReport.setStrengths(progressReportDetails.getStrengths());
        existingReport.setAreasForImprovement(progressReportDetails.getAreasForImprovement());
        existingReport.setRecommendations(progressReportDetails.getRecommendations());
        existingReport.setNextGoals(progressReportDetails.getNextGoals());
        existingReport.setTeacherNotes(progressReportDetails.getTeacherNotes());
        existingReport.setReportDate(progressReportDetails.getReportDate());
        
        existingReport.validate();
        return progressReportRepository.save(existingReport);
    }

    public void deleteProgressReport(Long id) {
        ProgressReport progressReport = getProgressReportById(id);
        progressReportRepository.delete(progressReport);
    }

    public ProgressReport getProgressReportById(Long id) {
        return progressReportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Progress report not found with id: " + id));
    }

    public List<ProgressReport> getAllProgressReports() {
        return progressReportRepository.findAll();
    }

    // ProgressReport query operations
    public List<ProgressReport> getProgressReportsByStudent(Long studentId) {
        return progressReportRepository.findByStudentId(studentId);
    }

    public List<ProgressReport> getProgressReportsByStudent(Student student) {
        return progressReportRepository.findByStudent(student);
    }

    public List<ProgressReport> getProgressReportsByReportType(String reportType) {
        return progressReportRepository.findByReportType(reportType);
    }

    public List<ProgressReport> getProgressReportsByReportPeriod(String reportPeriod) {
        return progressReportRepository.findByReportPeriod(reportPeriod);
    }

    // Combined query operations
    public List<ProgressReport> getProgressReportsByStudentAndType(Long studentId, String reportType) {
        return progressReportRepository.findByStudentIdAndReportType(studentId, reportType);
    }

    public List<ProgressReport> getProgressReportsByStudentAndPeriod(Long studentId, String reportPeriod) {
        return progressReportRepository.findByStudentIdAndReportPeriod(studentId, reportPeriod);
    }

    public List<ProgressReport> getProgressReportsByStudentTypeAndPeriod(Long studentId, String reportType, String reportPeriod) {
        return progressReportRepository.findByStudentIdAndReportTypeAndReportPeriod(studentId, reportType, reportPeriod);
    }

    // Advanced query operations
    public List<ProgressReport> getProgressReportsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return progressReportRepository.findByReportDateRange(startDate, endDate);
    }

    public List<ProgressReport> getProgressReportsByStudentAndDateRange(Long studentId, LocalDateTime startDate, LocalDateTime endDate) {
        return progressReportRepository.findByStudentIdAndReportDateRange(studentId, startDate, endDate);
    }

    public List<ProgressReport> getProgressReportsByProgressRange(Double minProgress, Double maxProgress) {
        return progressReportRepository.findByOverallProgressRange(minProgress, maxProgress);
    }

    public List<ProgressReport> getProgressReportsByStudentAndProgressRange(Long studentId, Double minProgress, Double maxProgress) {
        return progressReportRepository.findByStudentIdAndOverallProgressRange(studentId, minProgress, maxProgress);
    }

    // Latest report operations
    public List<ProgressReport> getLatestReportsForAllStudents() {
        return progressReportRepository.findLatestReportsForAllStudents();
    }

    public List<ProgressReport> getLatestReportsByStudent(Long studentId) {
        return progressReportRepository.findLatestReportsByStudent(studentId);
    }

    public List<ProgressReport> getLatestReportsByStudentAndType(Long studentId, String reportType) {
        return progressReportRepository.findLatestReportsByStudentAndType(studentId, reportType);
    }

    // Performance analysis operations
    public List<ProgressReport> getHighPerformingReports(Double threshold) {
        return progressReportRepository.findHighPerformingReports(threshold);
    }

    public List<ProgressReport> getLowPerformingReports(Double threshold) {
        return progressReportRepository.findLowPerformingReports(threshold);
    }

    public List<ProgressReport> getHighPerformingReportsByStudent(Long studentId, Double threshold) {
        return progressReportRepository.findHighPerformingReportsByStudent(studentId, threshold);
    }

    public List<ProgressReport> getLowPerformingReportsByStudent(Long studentId, Double threshold) {
        return progressReportRepository.findLowPerformingReportsByStudent(studentId, threshold);
    }

    // Statistics operations
    public List<Object[]> countReportsByStudent() {
        return progressReportRepository.countReportsByStudent();
    }

    public List<Object[]> countReportsByType() {
        return progressReportRepository.countReportsByType();
    }

    public List<Object[]> countReportsByPeriod() {
        return progressReportRepository.countReportsByPeriod();
    }

    public List<Object[]> getAverageProgressByStudent() {
        return progressReportRepository.getAverageProgressByStudent();
    }

    public List<Object[]> getAverageProgressByReportType() {
        return progressReportRepository.getAverageProgressByReportType();
    }

    public List<Object[]> getProgressTrendForStudent(Long studentId) {
        return progressReportRepository.getProgressTrendForStudent(studentId);
    }

    public List<Object[]> getOverallProgressTrend() {
        return progressReportRepository.getOverallProgressTrend();
    }

    public Object[] getOverallSkillAverages() {
        return progressReportRepository.getOverallSkillAverages();
    }

    public Object[] getSkillAveragesForStudent(Long studentId) {
        return progressReportRepository.getSkillAveragesForStudent(studentId);
    }

    public Object[] getOverallPerformanceStatistics() {
        return progressReportRepository.getOverallPerformanceStatistics();
    }

    public Object[] getPerformanceStatisticsForStudent(Long studentId) {
        return progressReportRepository.getPerformanceStatisticsForStudent(studentId);
    }

    // Date range operations
    public List<ProgressReport> getProgressReportsByCreationDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return progressReportRepository.findByCreationDateRange(startDate, endDate);
    }

    public List<ProgressReport> getProgressReportsByStudentAndCreationDateRange(Long studentId, LocalDateTime startDate, LocalDateTime endDate) {
        return progressReportRepository.findByStudentIdAndCreationDateRange(studentId, startDate, endDate);
    }

    // Utility operations
    public boolean progressReportExists(Long id) {
        return progressReportRepository.existsById(id);
    }

    public long getTotalProgressReportCount() {
        return progressReportRepository.count();
    }

    public boolean existsByStudentAndTypeAndPeriod(Long studentId, String reportType, String reportPeriod) {
        return progressReportRepository.existsByStudentAndTypeAndPeriod(studentId, reportType, reportPeriod);
    }

    public List<ProgressReport> getMostRecentReportsByType() {
        return progressReportRepository.findMostRecentReportsByType();
    }

    // Analytics operations
    public double getAverageOverallProgress() {
        Object[] stats = getOverallPerformanceStatistics();
        if (stats != null && stats.length >= 2) {
            return (Double) stats[1];
        }
        return 0.0;
    }

    public double getAverageProgressForStudent(Long studentId) {
        Object[] stats = getPerformanceStatisticsForStudent(studentId);
        if (stats != null && stats.length >= 2) {
            return (Double) stats[1];
        }
        return 0.0;
    }

    public double getHighestOverallProgress() {
        Object[] stats = getOverallPerformanceStatistics();
        if (stats != null && stats.length >= 4) {
            return (Double) stats[3];
        }
        return 0.0;
    }

    public double getLowestOverallProgress() {
        Object[] stats = getOverallPerformanceStatistics();
        if (stats != null && stats.length >= 3) {
            return (Double) stats[2];
        }
        return 0.0;
    }

    public long getHighPerformingStudentCount(Double threshold) {
        return getHighPerformingReports(threshold).stream()
                .map(report -> report.getStudent().getId())
                .distinct()
                .count();
    }

    public long getLowPerformingStudentCount(Double threshold) {
        return getLowPerformingReports(threshold).stream()
                .map(report -> report.getStudent().getId())
                .distinct()
                .count();
    }

    // Report generation operations
    public ProgressReport generateMonthlyReport(Long studentId, String period) {
        // Check if report already exists
        if (existsByStudentAndTypeAndPeriod(studentId, "MONTHLY", period)) {
            throw new IllegalArgumentException("Monthly report already exists for student " + studentId + " and period " + period);
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + studentId));

        // This is a simplified report generation - in a real application,
        // you would calculate progress based on lesson content completion,
        // practice logs, assessments, etc.
        ProgressReport report = new ProgressReport();
        report.setStudent(student);
        report.setReportType("MONTHLY");
        report.setReportPeriod(period);
        report.setOverallProgress(75.0); // Placeholder - would be calculated
        report.setTechnicalSkills(80.0);
        report.setTheoryKnowledge(70.0);
        report.setRepertoireSkills(75.0);
        report.setPracticeHabits(85.0);
        report.setReportDate(LocalDateTime.now());

        return createProgressReport(report);
    }

    public ProgressReport generateAssessmentReport(Long studentId, String period) {
        // Check if report already exists
        if (existsByStudentAndTypeAndPeriod(studentId, "ASSESSMENT", period)) {
            throw new IllegalArgumentException("Assessment report already exists for student " + studentId + " and period " + period);
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + studentId));

        ProgressReport report = new ProgressReport();
        report.setStudent(student);
        report.setReportType("ASSESSMENT");
        report.setReportPeriod(period);
        report.setOverallProgress(85.0); // Placeholder - would be calculated
        report.setTechnicalSkills(90.0);
        report.setTheoryKnowledge(80.0);
        report.setRepertoireSkills(85.0);
        report.setPracticeHabits(88.0);
        report.setReportDate(LocalDateTime.now());

        return createProgressReport(report);
    }
}
