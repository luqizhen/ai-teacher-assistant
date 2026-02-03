package com.pianoteacher.controller;

import com.pianoteacher.dto.ProgressReportDTO;
import com.pianoteacher.model.ProgressReport;
import com.pianoteacher.model.Student;
import com.pianoteacher.service.ProgressReportService;
import com.pianoteacher.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/progress-reports")
@CrossOrigin(origins = "*")
@Tag(name = "Progress Reports", description = "Operations for managing student progress reports and analytics")
public class ProgressReportController {

    private final ProgressReportService progressReportService;
    private final StudentService studentService;

    @Autowired
    public ProgressReportController(ProgressReportService progressReportService, StudentService studentService) {
        this.progressReportService = progressReportService;
        this.studentService = studentService;
    }

    // ProgressReport CRUD endpoints
    @PostMapping
    public ResponseEntity<ProgressReport> createProgressReport(@RequestBody ProgressReportDTO progressReportDTO) {
        try {
            // Convert DTO to entity
            ProgressReport progressReport = new ProgressReport();
            Student student = studentService.getStudentById(progressReportDTO.getStudentId());
            progressReport.setStudent(student);
            progressReport.setReportType(progressReportDTO.getReportType());
            progressReport.setReportPeriod(progressReportDTO.getReportPeriod());
            progressReport.setOverallProgress(progressReportDTO.getOverallProgress());
            progressReport.setTechnicalSkills(progressReportDTO.getTechnicalSkills());
            progressReport.setTheoryKnowledge(progressReportDTO.getTheoryKnowledge());
            progressReport.setRepertoireSkills(progressReportDTO.getRepertoireSkills());
            progressReport.setPracticeHabits(progressReportDTO.getPracticeHabits());
            progressReport.setStrengths(progressReportDTO.getStrengths());
            progressReport.setAreasForImprovement(progressReportDTO.getAreasForImprovement());
            progressReport.setRecommendations(progressReportDTO.getRecommendations());
            progressReport.setNextGoals(progressReportDTO.getNextGoals());
            progressReport.setTeacherNotes(progressReportDTO.getTeacherNotes());
            progressReport.setReportDate(progressReportDTO.getReportDate());
            
            ProgressReport createdReport = progressReportService.createProgressReport(progressReport);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdReport);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgressReport> getProgressReportById(@PathVariable Long id) {
        try {
            ProgressReport progressReport = progressReportService.getProgressReportById(id);
            return ResponseEntity.ok(progressReport);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ProgressReport>> getAllProgressReports() {
        List<ProgressReport> progressReports = progressReportService.getAllProgressReports();
        return ResponseEntity.ok(progressReports);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProgressReport> updateProgressReport(@PathVariable Long id, @Valid @RequestBody ProgressReportDTO progressReportDTO) {
        try {
            // Convert DTO to entity
            ProgressReport progressReportDetails = new ProgressReport();
            Student student = studentService.getStudentById(progressReportDTO.getStudentId());
            progressReportDetails.setStudent(student);
            progressReportDetails.setReportType(progressReportDTO.getReportType());
            progressReportDetails.setReportPeriod(progressReportDTO.getReportPeriod());
            progressReportDetails.setOverallProgress(progressReportDTO.getOverallProgress());
            progressReportDetails.setTechnicalSkills(progressReportDTO.getTechnicalSkills());
            progressReportDetails.setTheoryKnowledge(progressReportDTO.getTheoryKnowledge());
            progressReportDetails.setRepertoireSkills(progressReportDTO.getRepertoireSkills());
            progressReportDetails.setPracticeHabits(progressReportDTO.getPracticeHabits());
            progressReportDetails.setStrengths(progressReportDTO.getStrengths());
            progressReportDetails.setAreasForImprovement(progressReportDTO.getAreasForImprovement());
            progressReportDetails.setRecommendations(progressReportDTO.getRecommendations());
            progressReportDetails.setNextGoals(progressReportDTO.getNextGoals());
            progressReportDetails.setTeacherNotes(progressReportDTO.getTeacherNotes());
            progressReportDetails.setReportDate(progressReportDTO.getReportDate());
            
            ProgressReport updatedReport = progressReportService.updateProgressReport(id, progressReportDetails);
            return ResponseEntity.ok(updatedReport);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgressReport(@PathVariable Long id) {
        try {
            progressReportService.deleteProgressReport(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ProgressReport query endpoints
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<ProgressReport>> getProgressReportsByStudent(@PathVariable Long studentId) {
        List<ProgressReport> progressReports = progressReportService.getProgressReportsByStudent(studentId);
        return ResponseEntity.ok(progressReports);
    }

    @GetMapping("/report-type/{reportType}")
    public ResponseEntity<List<ProgressReport>> getProgressReportsByReportType(@PathVariable String reportType) {
        List<ProgressReport> progressReports = progressReportService.getProgressReportsByReportType(reportType);
        return ResponseEntity.ok(progressReports);
    }

    @GetMapping("/report-period/{reportPeriod}")
    public ResponseEntity<List<ProgressReport>> getProgressReportsByReportPeriod(@PathVariable String reportPeriod) {
        List<ProgressReport> progressReports = progressReportService.getProgressReportsByReportPeriod(reportPeriod);
        return ResponseEntity.ok(progressReports);
    }

    // Combined query endpoints
    @GetMapping("/student/{studentId}/report-type/{reportType}")
    public ResponseEntity<List<ProgressReport>> getProgressReportsByStudentAndType(
            @PathVariable Long studentId, @PathVariable String reportType) {
        List<ProgressReport> progressReports = progressReportService.getProgressReportsByStudentAndType(studentId, reportType);
        return ResponseEntity.ok(progressReports);
    }

    @GetMapping("/student/{studentId}/report-period/{reportPeriod}")
    public ResponseEntity<List<ProgressReport>> getProgressReportsByStudentAndPeriod(
            @PathVariable Long studentId, @PathVariable String reportPeriod) {
        List<ProgressReport> progressReports = progressReportService.getProgressReportsByStudentAndPeriod(studentId, reportPeriod);
        return ResponseEntity.ok(progressReports);
    }

    @GetMapping("/student/{studentId}/report-type/{reportType}/report-period/{reportPeriod}")
    public ResponseEntity<List<ProgressReport>> getProgressReportsByStudentTypeAndPeriod(
            @PathVariable Long studentId, @PathVariable String reportType, @PathVariable String reportPeriod) {
        List<ProgressReport> progressReports = progressReportService.getProgressReportsByStudentTypeAndPeriod(studentId, reportType, reportPeriod);
        return ResponseEntity.ok(progressReports);
    }

    // Advanced query endpoints
    @GetMapping("/date-range")
    public ResponseEntity<List<ProgressReport>> getProgressReportsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<ProgressReport> progressReports = progressReportService.getProgressReportsByDateRange(startDate, endDate);
        return ResponseEntity.ok(progressReports);
    }

    @GetMapping("/student/{studentId}/date-range")
    public ResponseEntity<List<ProgressReport>> getProgressReportsByStudentAndDateRange(
            @PathVariable Long studentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<ProgressReport> progressReports = progressReportService.getProgressReportsByStudentAndDateRange(studentId, startDate, endDate);
        return ResponseEntity.ok(progressReports);
    }

    @GetMapping("/progress-range")
    public ResponseEntity<List<ProgressReport>> getProgressReportsByProgressRange(
            @RequestParam Double minProgress, @RequestParam Double maxProgress) {
        List<ProgressReport> progressReports = progressReportService.getProgressReportsByProgressRange(minProgress, maxProgress);
        return ResponseEntity.ok(progressReports);
    }

    @GetMapping("/student/{studentId}/progress-range")
    public ResponseEntity<List<ProgressReport>> getProgressReportsByStudentAndProgressRange(
            @PathVariable Long studentId, @RequestParam Double minProgress, @RequestParam Double maxProgress) {
        List<ProgressReport> progressReports = progressReportService.getProgressReportsByStudentAndProgressRange(studentId, minProgress, maxProgress);
        return ResponseEntity.ok(progressReports);
    }

    // Latest report endpoints
    @GetMapping("/latest/all-students")
    public ResponseEntity<List<ProgressReport>> getLatestReportsForAllStudents() {
        List<ProgressReport> progressReports = progressReportService.getLatestReportsForAllStudents();
        return ResponseEntity.ok(progressReports);
    }

    @GetMapping("/latest/student/{studentId}")
    public ResponseEntity<List<ProgressReport>> getLatestReportsByStudent(@PathVariable Long studentId) {
        List<ProgressReport> progressReports = progressReportService.getLatestReportsByStudent(studentId);
        return ResponseEntity.ok(progressReports);
    }

    @GetMapping("/latest/student/{studentId}/report-type/{reportType}")
    public ResponseEntity<List<ProgressReport>> getLatestReportsByStudentAndType(
            @PathVariable Long studentId, @PathVariable String reportType) {
        List<ProgressReport> progressReports = progressReportService.getLatestReportsByStudentAndType(studentId, reportType);
        return ResponseEntity.ok(progressReports);
    }

    // Performance analysis endpoints
    @GetMapping("/high-performing")
    public ResponseEntity<List<ProgressReport>> getHighPerformingReports(@RequestParam Double threshold) {
        List<ProgressReport> progressReports = progressReportService.getHighPerformingReports(threshold);
        return ResponseEntity.ok(progressReports);
    }

    @GetMapping("/low-performing")
    public ResponseEntity<List<ProgressReport>> getLowPerformingReports(@RequestParam Double threshold) {
        List<ProgressReport> progressReports = progressReportService.getLowPerformingReports(threshold);
        return ResponseEntity.ok(progressReports);
    }

    @GetMapping("/student/{studentId}/high-performing")
    public ResponseEntity<List<ProgressReport>> getHighPerformingReportsByStudent(
            @PathVariable Long studentId, @RequestParam Double threshold) {
        List<ProgressReport> progressReports = progressReportService.getHighPerformingReportsByStudent(studentId, threshold);
        return ResponseEntity.ok(progressReports);
    }

    @GetMapping("/student/{studentId}/low-performing")
    public ResponseEntity<List<ProgressReport>> getLowPerformingReportsByStudent(
            @PathVariable Long studentId, @RequestParam Double threshold) {
        List<ProgressReport> progressReports = progressReportService.getLowPerformingReportsByStudent(studentId, threshold);
        return ResponseEntity.ok(progressReports);
    }

    // Statistics endpoints
    @GetMapping("/stats/student-counts")
    public ResponseEntity<List<Map<String, Object>>> getStudentCounts() {
        List<Object[]> results = progressReportService.countReportsByStudent();
        List<Map<String, Object>> stats = results.stream()
                .map(result -> Map.of(
                        "studentId", result[0],
                        "reportCount", result[1]
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/report-type-counts")
    public ResponseEntity<List<Map<String, Object>>> getReportTypeCounts() {
        List<Object[]> results = progressReportService.countReportsByType();
        List<Map<String, Object>> stats = results.stream()
                .map(result -> Map.of(
                        "reportType", result[0],
                        "count", result[1]
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/period-counts")
    public ResponseEntity<List<Map<String, Object>>> getPeriodCounts() {
        List<Object[]> results = progressReportService.countReportsByPeriod();
        List<Map<String, Object>> stats = results.stream()
                .map(result -> Map.of(
                        "reportPeriod", result[0],
                        "count", result[1]
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/average-progress-by-student")
    public ResponseEntity<List<Map<String, Object>>> getAverageProgressByStudent() {
        List<Object[]> results = progressReportService.getAverageProgressByStudent();
        List<Map<String, Object>> stats = results.stream()
                .map(result -> Map.of(
                        "studentId", result[0],
                        "averageProgress", result[1]
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/average-progress-by-type")
    public ResponseEntity<List<Map<String, Object>>> getAverageProgressByReportType() {
        List<Object[]> results = progressReportService.getAverageProgressByReportType();
        List<Map<String, Object>> stats = results.stream()
                .map(result -> Map.of(
                        "reportType", result[0],
                        "averageProgress", result[1]
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/overall-performance")
    public ResponseEntity<Object[]> getOverallPerformanceStatistics() {
        Object[] stats = progressReportService.getOverallPerformanceStatistics();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/student/{studentId}/performance")
    public ResponseEntity<Object[]> getPerformanceStatisticsForStudent(@PathVariable Long studentId) {
        Object[] stats = progressReportService.getPerformanceStatisticsForStudent(studentId);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/skill-averages")
    public ResponseEntity<Object[]> getOverallSkillAverages() {
        Object[] stats = progressReportService.getOverallSkillAverages();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/student/{studentId}/skill-averages")
    public ResponseEntity<Object[]> getSkillAveragesForStudent(@PathVariable Long studentId) {
        Object[] stats = progressReportService.getSkillAveragesForStudent(studentId);
        return ResponseEntity.ok(stats);
    }

    // Progress trend endpoints
    @GetMapping("/trends/student/{studentId}")
    public ResponseEntity<List<Map<String, Object>>> getProgressTrendForStudent(@PathVariable Long studentId) {
        List<Object[]> results = progressReportService.getProgressTrendForStudent(studentId);
        List<Map<String, Object>> trends = results.stream()
                .map(result -> Map.of(
                        "reportDate", result[0],
                        "overallProgress", result[1]
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(trends);
    }

    @GetMapping("/trends/overall")
    public ResponseEntity<List<Map<String, Object>>> getOverallProgressTrend() {
        List<Object[]> results = progressReportService.getOverallProgressTrend();
        List<Map<String, Object>> trends = results.stream()
                .map(result -> Map.of(
                        "reportDate", result[0],
                        "averageProgress", result[1]
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(trends);
    }

    // Report generation endpoints
    @PostMapping("/generate/monthly")
    public ResponseEntity<ProgressReport> generateMonthlyReport(
            @RequestParam Long studentId, @RequestParam String period) {
        try {
            ProgressReport report = progressReportService.generateMonthlyReport(studentId, period);
            return ResponseEntity.status(HttpStatus.CREATED).body(report);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/generate/assessment")
    public ResponseEntity<ProgressReport> generateAssessmentReport(
            @RequestParam Long studentId, @RequestParam String period) {
        try {
            ProgressReport report = progressReportService.generateAssessmentReport(studentId, period);
            return ResponseEntity.status(HttpStatus.CREATED).body(report);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Utility endpoints
    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> checkProgressReportExists(@PathVariable Long id) {
        boolean exists = progressReportService.progressReportExists(id);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getTotalProgressReportCount() {
        long count = progressReportService.getTotalProgressReportCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/exists/student/{studentId}/report-type/{reportType}/period/{reportPeriod}")
    public ResponseEntity<Boolean> checkReportExists(
            @PathVariable Long studentId, @PathVariable String reportType, @PathVariable String reportPeriod) {
        boolean exists = progressReportService.existsByStudentAndTypeAndPeriod(studentId, reportType, reportPeriod);
        return ResponseEntity.ok(exists);
    }

    // Error handling
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException e) {
        Map<String, String> error = Map.of(
                "error", "Validation Error",
                "message", e.getMessage(),
                "timestamp", LocalDateTime.now().toString()
        );
        return ResponseEntity.badRequest().body(error);
    }
}
