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

/**
 * Lessons Controller - Provides simplified lesson management endpoints
 * This controller acts as a facade over the LessonContentService to match frontend expectations
 */
@RestController
@RequestMapping("/api/lessons")
@CrossOrigin(origins = "*")
public class LessonsController {

    private final LessonContentService lessonContentService;
    private final StudentService studentService;

    @Autowired
    public LessonsController(LessonContentService lessonContentService, StudentService studentService) {
        this.lessonContentService = lessonContentService;
        this.studentService = studentService;
    }

    /**
     * Get all lessons
     */
    @GetMapping
    public ResponseEntity<List<LessonContent>> getAllLessons() {
        List<LessonContent> lessons = lessonContentService.getAllLessonContent();
        return ResponseEntity.ok(lessons);
    }

    /**
     * Get recent lessons (last 30 days)
     */
    @GetMapping(params = "recent=true")
    public ResponseEntity<List<LessonContent>> getRecentLessons() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        List<LessonContent> recentLessons = lessonContentService.getLessonContentByCompletionDateRange(thirtyDaysAgo, LocalDateTime.now());
        return ResponseEntity.ok(recentLessons);
    }

    /**
     * Get lessons by student ID
     */
    @GetMapping(params = "studentId")
    public ResponseEntity<List<LessonContent>> getLessonsByStudent(@RequestParam Long studentId) {
        List<LessonContent> lessons = lessonContentService.getLessonContentByStudent(studentId);
        return ResponseEntity.ok(lessons);
    }

    /**
     * Get lesson by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<LessonContent> getLessonById(@PathVariable Long id) {
        try {
            LessonContent lesson = lessonContentService.getLessonContentById(id);
            return ResponseEntity.ok(lesson);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Create new lesson
     */
    @PostMapping
    public ResponseEntity<LessonContent> createLesson(@Valid @RequestBody LessonContentDTO lessonDTO) {
        try {
            // Convert DTO to entity
            LessonContent lesson = new LessonContent();
            Student student = studentService.getStudentById(lessonDTO.getStudentId());
            lesson.setStudent(student);
            lesson.setTitle(lessonDTO.getTitle());
            lesson.setDescription(lessonDTO.getDescription());
            lesson.setContentType(lessonDTO.getContentType());
            lesson.setDifficultyLevel(lessonDTO.getDifficultyLevel());
            lesson.setEstimatedDuration(lessonDTO.getEstimatedDuration());
            lesson.setNotes(lessonDTO.getNotes());
            lesson.setCompleted(lessonDTO.getCompleted());
            lesson.setCompletionDate(lessonDTO.getCompletionDate());
            
            LessonContent createdLesson = lessonContentService.createLessonContent(lesson);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdLesson);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Update lesson
     */
    @PutMapping("/{id}")
    public ResponseEntity<LessonContent> updateLesson(@PathVariable Long id, @Valid @RequestBody LessonContentDTO lessonDTO) {
        try {
            // Convert DTO to entity
            LessonContent lessonDetails = new LessonContent();
            Student student = studentService.getStudentById(lessonDTO.getStudentId());
            lessonDetails.setStudent(student);
            lessonDetails.setTitle(lessonDTO.getTitle());
            lessonDetails.setDescription(lessonDTO.getDescription());
            lessonDetails.setContentType(lessonDTO.getContentType());
            lessonDetails.setDifficultyLevel(lessonDTO.getDifficultyLevel());
            lessonDetails.setEstimatedDuration(lessonDTO.getEstimatedDuration());
            lessonDetails.setNotes(lessonDTO.getNotes());
            lessonDetails.setCompleted(lessonDTO.getCompleted());
            lessonDetails.setCompletionDate(lessonDTO.getCompletionDate());
            
            LessonContent updatedLesson = lessonContentService.updateLessonContent(id, lessonDetails);
            return ResponseEntity.ok(updatedLesson);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Delete lesson
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id) {
        try {
            lessonContentService.deleteLessonContent(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get lesson statistics
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getLessonStats() {
        long totalCount = lessonContentService.getTotalLessonContentCount();
        long completedCount = lessonContentService.getCompletedLessonContentCount();
        long incompleteCount = lessonContentService.getIncompleteLessonContentCount();
        double completionRate = lessonContentService.getCompletionRate();
        
        Map<String, Object> stats = Map.of(
            "totalLessons", totalCount,
            "completedLessons", completedCount,
            "incompleteLessons", incompleteCount,
            "completionRate", completionRate
        );
        
        return ResponseEntity.ok(stats);
    }

    /**
     * Get lessons by content type
     */
    @GetMapping("/type/{contentType}")
    public ResponseEntity<List<LessonContent>> getLessonsByContentType(@PathVariable String contentType) {
        List<LessonContent> lessons = lessonContentService.getLessonContentByContentType(contentType);
        return ResponseEntity.ok(lessons);
    }

    /**
     * Get lessons by difficulty level
     */
    @GetMapping("/difficulty/{level}")
    public ResponseEntity<List<LessonContent>> getLessonsByDifficulty(@PathVariable Integer level) {
        List<LessonContent> lessons = lessonContentService.getLessonContentByDifficultyLevel(level);
        return ResponseEntity.ok(lessons);
    }

    /**
     * Get completed lessons
     */
    @GetMapping("/completed")
    public ResponseEntity<List<LessonContent>> getCompletedLessons() {
        List<LessonContent> lessons = lessonContentService.getCompletedLessonContent();
        return ResponseEntity.ok(lessons);
    }

    /**
     * Get incomplete lessons
     */
    @GetMapping("/incomplete")
    public ResponseEntity<List<LessonContent>> getIncompleteLessons() {
        List<LessonContent> lessons = lessonContentService.getIncompleteLessonContent();
        return ResponseEntity.ok(lessons);
    }

    /**
     * Mark lesson as completed
     */
    @PutMapping("/{id}/complete")
    public ResponseEntity<LessonContent> markLessonAsCompleted(@PathVariable Long id) {
        try {
            LessonContent lesson = lessonContentService.markAsCompleted(id);
            return ResponseEntity.ok(lesson);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Mark lesson as incomplete
     */
    @PutMapping("/{id}/incomplete")
    public ResponseEntity<LessonContent> markLessonAsIncomplete(@PathVariable Long id) {
        try {
            LessonContent lesson = lessonContentService.markAsIncomplete(id);
            return ResponseEntity.ok(lesson);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Search lessons
     */
    @GetMapping("/search")
    public ResponseEntity<List<LessonContent>> searchLessons(@RequestParam String term) {
        List<LessonContent> lessons = lessonContentService.searchLessonContent(term);
        return ResponseEntity.ok(lessons);
    }

    /**
     * Get lessons for a specific student
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<LessonContent>> getStudentLessons(@PathVariable Long studentId) {
        List<LessonContent> lessons = lessonContentService.getLessonContentByStudent(studentId);
        return ResponseEntity.ok(lessons);
    }

    /**
     * Get completed lessons for a specific student
     */
    @GetMapping("/student/{studentId}/completed")
    public ResponseEntity<List<LessonContent>> getStudentCompletedLessons(@PathVariable Long studentId) {
        List<LessonContent> lessons = lessonContentService.getLessonContentByStudentAndCompletionStatus(studentId, true);
        return ResponseEntity.ok(lessons);
    }

    /**
     * Get incomplete lessons for a specific student
     */
    @GetMapping("/student/{studentId}/incomplete")
    public ResponseEntity<List<LessonContent>> getStudentIncompleteLessons(@PathVariable Long studentId) {
        List<LessonContent> lessons = lessonContentService.getLessonContentByStudentAndCompletionStatus(studentId, false);
        return ResponseEntity.ok(lessons);
    }

    /**
     * Error handling
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException e) {
        Map<String, String> error = Map.of(
            "error", "Validation Error",
            "message", e.getMessage(),
            "timestamp", LocalDateTime.now().toString()
        );
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception e) {
        Map<String, String> error = Map.of(
            "error", "Internal Server Error",
            "message", "An unexpected error occurred: " + e.getMessage(),
            "timestamp", LocalDateTime.now().toString()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
