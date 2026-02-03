package com.pianoteacher.controller;

import com.pianoteacher.dto.LessonContentDTO;
import com.pianoteacher.model.LessonContent;
import com.pianoteacher.model.Student;
import com.pianoteacher.service.LessonContentService;
import com.pianoteacher.service.StudentService;
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
@RequestMapping("/api/lesson-content")
@CrossOrigin(origins = "*")
public class LessonContentController {

    private final LessonContentService lessonContentService;
    private final StudentService studentService;

    @Autowired
    public LessonContentController(LessonContentService lessonContentService, StudentService studentService) {
        this.lessonContentService = lessonContentService;
        this.studentService = studentService;
    }

    // LessonContent CRUD endpoints
    @PostMapping
    public ResponseEntity<LessonContent> createLessonContent(@RequestBody LessonContentDTO lessonContentDTO) {
        try {
            // Convert DTO to entity
            LessonContent lessonContent = new LessonContent();
            Student student = studentService.getStudentById(lessonContentDTO.getStudentId());
            lessonContent.setStudent(student);
            lessonContent.setTitle(lessonContentDTO.getTitle());
            lessonContent.setDescription(lessonContentDTO.getDescription());
            lessonContent.setContentType(lessonContentDTO.getContentType());
            lessonContent.setDifficultyLevel(lessonContentDTO.getDifficultyLevel());
            lessonContent.setEstimatedDuration(lessonContentDTO.getEstimatedDuration());
            lessonContent.setNotes(lessonContentDTO.getNotes());
            
            LessonContent createdContent = lessonContentService.createLessonContent(lessonContent);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdContent);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonContent> getLessonContentById(@PathVariable Long id) {
        try {
            LessonContent lessonContent = lessonContentService.getLessonContentById(id);
            return ResponseEntity.ok(lessonContent);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<LessonContent>> getAllLessonContent() {
        List<LessonContent> lessonContent = lessonContentService.getAllLessonContent();
        return ResponseEntity.ok(lessonContent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LessonContent> updateLessonContent(@PathVariable Long id, @Valid @RequestBody LessonContentDTO lessonContentDTO) {
        try {
            // Convert DTO to entity
            LessonContent lessonContentDetails = new LessonContent();
            Student student = studentService.getStudentById(lessonContentDTO.getStudentId());
            lessonContentDetails.setStudent(student);
            lessonContentDetails.setTitle(lessonContentDTO.getTitle());
            lessonContentDetails.setDescription(lessonContentDTO.getDescription());
            lessonContentDetails.setContentType(lessonContentDTO.getContentType());
            lessonContentDetails.setDifficultyLevel(lessonContentDTO.getDifficultyLevel());
            lessonContentDetails.setEstimatedDuration(lessonContentDTO.getEstimatedDuration());
            lessonContentDetails.setNotes(lessonContentDTO.getNotes());
            lessonContentDetails.setCompleted(lessonContentDTO.getCompleted());
            lessonContentDetails.setCompletionDate(lessonContentDTO.getCompletionDate());
            
            LessonContent updatedContent = lessonContentService.updateLessonContent(id, lessonContentDetails);
            return ResponseEntity.ok(updatedContent);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLessonContent(@PathVariable Long id) {
        try {
            lessonContentService.deleteLessonContent(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // LessonContent query endpoints
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<LessonContent>> getLessonContentByStudent(@PathVariable Long studentId) {
        List<LessonContent> lessonContent = lessonContentService.getLessonContentByStudent(studentId);
        return ResponseEntity.ok(lessonContent);
    }

    @GetMapping("/content-type/{contentType}")
    public ResponseEntity<List<LessonContent>> getLessonContentByContentType(@PathVariable String contentType) {
        List<LessonContent> lessonContent = lessonContentService.getLessonContentByContentType(contentType);
        return ResponseEntity.ok(lessonContent);
    }

    @GetMapping("/difficulty/{difficultyLevel}")
    public ResponseEntity<List<LessonContent>> getLessonContentByDifficultyLevel(@PathVariable Integer difficultyLevel) {
        List<LessonContent> lessonContent = lessonContentService.getLessonContentByDifficultyLevel(difficultyLevel);
        return ResponseEntity.ok(lessonContent);
    }

    @GetMapping("/completed")
    public ResponseEntity<List<LessonContent>> getCompletedLessonContent() {
        List<LessonContent> lessonContent = lessonContentService.getCompletedLessonContent();
        return ResponseEntity.ok(lessonContent);
    }

    @GetMapping("/incomplete")
    public ResponseEntity<List<LessonContent>> getIncompleteLessonContent() {
        List<LessonContent> lessonContent = lessonContentService.getIncompleteLessonContent();
        return ResponseEntity.ok(lessonContent);
    }

    // Combined query endpoints
    @GetMapping("/student/{studentId}/content-type/{contentType}")
    public ResponseEntity<List<LessonContent>> getLessonContentByStudentAndContentType(
            @PathVariable Long studentId, @PathVariable String contentType) {
        List<LessonContent> lessonContent = lessonContentService.getLessonContentByStudentAndContentType(studentId, contentType);
        return ResponseEntity.ok(lessonContent);
    }

    @GetMapping("/student/{studentId}/difficulty/{difficultyLevel}")
    public ResponseEntity<List<LessonContent>> getLessonContentByStudentAndDifficultyLevel(
            @PathVariable Long studentId, @PathVariable Integer difficultyLevel) {
        List<LessonContent> lessonContent = lessonContentService.getLessonContentByStudentAndDifficultyLevel(studentId, difficultyLevel);
        return ResponseEntity.ok(lessonContent);
    }

    @GetMapping("/student/{studentId}/completed/{completed}")
    public ResponseEntity<List<LessonContent>> getLessonContentByStudentAndCompletionStatus(
            @PathVariable Long studentId, @PathVariable Boolean completed) {
        List<LessonContent> lessonContent = lessonContentService.getLessonContentByStudentAndCompletionStatus(studentId, completed);
        return ResponseEntity.ok(lessonContent);
    }

    // Advanced query endpoints
    @GetMapping("/difficulty-range")
    public ResponseEntity<List<LessonContent>> getLessonContentByDifficultyRange(
            @RequestParam Integer minLevel, @RequestParam Integer maxLevel) {
        List<LessonContent> lessonContent = lessonContentService.getLessonContentByDifficultyRange(minLevel, maxLevel);
        return ResponseEntity.ok(lessonContent);
    }

    @GetMapping("/student/{studentId}/difficulty-range")
    public ResponseEntity<List<LessonContent>> getLessonContentByStudentAndDifficultyRange(
            @PathVariable Long studentId, @RequestParam Integer minLevel, @RequestParam Integer maxLevel) {
        List<LessonContent> lessonContent = lessonContentService.getLessonContentByStudentAndDifficultyRange(studentId, minLevel, maxLevel);
        return ResponseEntity.ok(lessonContent);
    }

    @GetMapping("/duration-range")
    public ResponseEntity<List<LessonContent>> getLessonContentByDurationRange(
            @RequestParam Integer minDuration, @RequestParam Integer maxDuration) {
        List<LessonContent> lessonContent = lessonContentService.getLessonContentByDurationRange(minDuration, maxDuration);
        return ResponseEntity.ok(lessonContent);
    }

    @GetMapping("/student/{studentId}/duration-range")
    public ResponseEntity<List<LessonContent>> getLessonContentByStudentAndDurationRange(
            @PathVariable Long studentId, @RequestParam Integer minDuration, @RequestParam Integer maxDuration) {
        List<LessonContent> lessonContent = lessonContentService.getLessonContentByStudentAndDurationRange(studentId, minDuration, maxDuration);
        return ResponseEntity.ok(lessonContent);
    }

    @GetMapping("/completion-date-range")
    public ResponseEntity<List<LessonContent>> getLessonContentByCompletionDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<LessonContent> lessonContent = lessonContentService.getLessonContentByCompletionDateRange(startDate, endDate);
        return ResponseEntity.ok(lessonContent);
    }

    @GetMapping("/student/{studentId}/completion-date-range")
    public ResponseEntity<List<LessonContent>> getLessonContentByStudentAndCompletionDateRange(
            @PathVariable Long studentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<LessonContent> lessonContent = lessonContentService.getLessonContentByStudentAndCompletionDateRange(studentId, startDate, endDate);
        return ResponseEntity.ok(lessonContent);
    }

    // Search endpoints
    @GetMapping("/search")
    public ResponseEntity<List<LessonContent>> searchLessonContent(@RequestParam String searchTerm) {
        List<LessonContent> lessonContent = lessonContentService.searchLessonContent(searchTerm);
        return ResponseEntity.ok(lessonContent);
    }

    @GetMapping("/student/{studentId}/search")
    public ResponseEntity<List<LessonContent>> searchLessonContentByStudent(
            @PathVariable Long studentId, @RequestParam String searchTerm) {
        List<LessonContent> lessonContent = lessonContentService.searchLessonContentByStudent(studentId, searchTerm);
        return ResponseEntity.ok(lessonContent);
    }

    // Progress tracking endpoints
    @GetMapping("/student/{studentId}/uncompleted")
    public ResponseEntity<List<LessonContent>> getUncompletedLessonContentByStudent(@PathVariable Long studentId) {
        List<LessonContent> lessonContent = lessonContentService.getUncompletedLessonContentByStudent(studentId);
        return ResponseEntity.ok(lessonContent);
    }

    @GetMapping("/recently-completed")
    public ResponseEntity<List<LessonContent>> getRecentlyCompletedLessonContent() {
        List<LessonContent> lessonContent = lessonContentService.getRecentlyCompletedLessonContent();
        return ResponseEntity.ok(lessonContent);
    }

    @GetMapping("/student/{studentId}/recently-completed")
    public ResponseEntity<List<LessonContent>> getRecentlyCompletedLessonContentByStudent(@PathVariable Long studentId) {
        List<LessonContent> lessonContent = lessonContentService.getRecentlyCompletedLessonContentByStudent(studentId);
        return ResponseEntity.ok(lessonContent);
    }

    // Completion management endpoints
    @PutMapping("/{id}/complete")
    public ResponseEntity<LessonContent> markAsCompleted(@PathVariable Long id) {
        try {
            LessonContent lessonContent = lessonContentService.markAsCompleted(id);
            return ResponseEntity.ok(lessonContent);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/incomplete")
    public ResponseEntity<LessonContent> markAsIncomplete(@PathVariable Long id) {
        try {
            LessonContent lessonContent = lessonContentService.markAsIncomplete(id);
            return ResponseEntity.ok(lessonContent);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Statistics endpoints
    @GetMapping("/stats/overview")
    public ResponseEntity<Object[]> getOverallCompletionStats() {
        Object[] stats = lessonContentService.getOverallCompletionStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/student/{studentId}")
    public ResponseEntity<Object[]> getStudentCompletionStats(@PathVariable Long studentId) {
        Object[] stats = lessonContentService.getStudentCompletionStats(studentId);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/completion-rate")
    public ResponseEntity<Double> getCompletionRate() {
        double rate = lessonContentService.getCompletionRate();
        return ResponseEntity.ok(rate);
    }

    @GetMapping("/stats/student/{studentId}/completion-rate")
    public ResponseEntity<Double> getStudentCompletionRate(@PathVariable Long studentId) {
        double rate = lessonContentService.getStudentCompletionRate(studentId);
        return ResponseEntity.ok(rate);
    }

    @GetMapping("/stats/content-type-counts")
    public ResponseEntity<List<Map<String, Object>>> getContentTypeCounts() {
        List<Object[]> results = lessonContentService.countByContentType();
        List<Map<String, Object>> stats = results.stream()
                .map(result -> Map.of(
                        "contentType", result[0],
                        "count", result[1]
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/difficulty-counts")
    public ResponseEntity<List<Map<String, Object>>> getDifficultyLevelCounts() {
        List<Object[]> results = lessonContentService.countByDifficultyLevel();
        List<Map<String, Object>> stats = results.stream()
                .map(result -> Map.of(
                        "difficultyLevel", result[0],
                        "count", result[1]
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/student-counts")
    public ResponseEntity<List<Map<String, Object>>> getStudentCounts() {
        List<Object[]> results = lessonContentService.countByStudent();
        List<Map<String, Object>> stats = results.stream()
                .map(result -> Map.of(
                        "studentId", result[0],
                        "contentCount", result[1]
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/completion-status-counts")
    public ResponseEntity<List<Map<String, Object>>> getCompletionStatusCounts() {
        List<Object[]> results = lessonContentService.countByCompletionStatus();
        List<Map<String, Object>> stats = results.stream()
                .map(result -> Map.of(
                        "completed", result[0],
                        "count", result[1]
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(stats);
    }

    // Utility endpoints
    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> checkLessonContentExists(@PathVariable Long id) {
        boolean exists = lessonContentService.lessonContentExists(id);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getTotalLessonContentCount() {
        long count = lessonContentService.getTotalLessonContentCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/completed")
    public ResponseEntity<Long> getCompletedLessonContentCount() {
        long count = lessonContentService.getCompletedLessonContentCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/incomplete")
    public ResponseEntity<Long> getIncompleteLessonContentCount() {
        long count = lessonContentService.getIncompleteLessonContentCount();
        return ResponseEntity.ok(count);
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
