package com.pianoteacher.service;

import com.pianoteacher.model.LessonContent;
import com.pianoteacher.model.Student;
import com.pianoteacher.repository.LessonContentRepository;
import com.pianoteacher.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LessonContentService {

    private final LessonContentRepository lessonContentRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public LessonContentService(LessonContentRepository lessonContentRepository, StudentRepository studentRepository) {
        this.lessonContentRepository = lessonContentRepository;
        this.studentRepository = studentRepository;
    }

    // LessonContent CRUD operations
    public LessonContent createLessonContent(LessonContent lessonContent) {
        lessonContent.validate();
        
        // Check if student exists
        if (!studentRepository.existsById(lessonContent.getStudent().getId())) {
            throw new IllegalArgumentException("Student not found with id: " + lessonContent.getStudent().getId());
        }
        
        return lessonContentRepository.save(lessonContent);
    }

    public LessonContent updateLessonContent(Long id, LessonContent lessonContentDetails) {
        LessonContent existingContent = getLessonContentById(id);
        
        // Check if student exists
        if (!studentRepository.existsById(lessonContentDetails.getStudent().getId())) {
            throw new IllegalArgumentException("Student not found with id: " + lessonContentDetails.getStudent().getId());
        }
        
        // Update content details
        existingContent.setStudent(lessonContentDetails.getStudent());
        existingContent.setTitle(lessonContentDetails.getTitle());
        existingContent.setDescription(lessonContentDetails.getDescription());
        existingContent.setContentType(lessonContentDetails.getContentType());
        existingContent.setDifficultyLevel(lessonContentDetails.getDifficultyLevel());
        existingContent.setEstimatedDuration(lessonContentDetails.getEstimatedDuration());
        existingContent.setNotes(lessonContentDetails.getNotes());
        existingContent.setCompleted(lessonContentDetails.getCompleted());
        existingContent.setCompletionDate(lessonContentDetails.getCompletionDate());
        
        existingContent.validate();
        return lessonContentRepository.save(existingContent);
    }

    public void deleteLessonContent(Long id) {
        LessonContent lessonContent = getLessonContentById(id);
        lessonContentRepository.delete(lessonContent);
    }

    public LessonContent getLessonContentById(Long id) {
        return lessonContentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lesson content not found with id: " + id));
    }

    public List<LessonContent> getAllLessonContent() {
        return lessonContentRepository.findAll();
    }

    // LessonContent query operations
    public List<LessonContent> getLessonContentByStudent(Long studentId) {
        return lessonContentRepository.findByStudentId(studentId);
    }

    public List<LessonContent> getLessonContentByStudent(Student student) {
        return lessonContentRepository.findByStudent(student);
    }

    public List<LessonContent> getLessonContentByContentType(String contentType) {
        return lessonContentRepository.findByContentType(contentType);
    }

    public List<LessonContent> getLessonContentByDifficultyLevel(Integer difficultyLevel) {
        return lessonContentRepository.findByDifficultyLevel(difficultyLevel);
    }

    public List<LessonContent> getCompletedLessonContent() {
        return lessonContentRepository.findByCompleted(true);
    }

    public List<LessonContent> getIncompleteLessonContent() {
        return lessonContentRepository.findByCompleted(false);
    }

    // Combined query operations
    public List<LessonContent> getLessonContentByStudentAndContentType(Long studentId, String contentType) {
        return lessonContentRepository.findByStudentIdAndContentType(studentId, contentType);
    }

    public List<LessonContent> getLessonContentByStudentAndDifficultyLevel(Long studentId, Integer difficultyLevel) {
        return lessonContentRepository.findByStudentIdAndDifficultyLevel(studentId, difficultyLevel);
    }

    public List<LessonContent> getLessonContentByStudentAndCompletionStatus(Long studentId, Boolean completed) {
        return lessonContentRepository.findByStudentIdAndCompleted(studentId, completed);
    }

    // Advanced query operations
    public List<LessonContent> getLessonContentByDifficultyRange(Integer minLevel, Integer maxLevel) {
        return lessonContentRepository.findByDifficultyRange(minLevel, maxLevel);
    }

    public List<LessonContent> getLessonContentByStudentAndDifficultyRange(Long studentId, Integer minLevel, Integer maxLevel) {
        return lessonContentRepository.findByStudentIdAndDifficultyRange(studentId, minLevel, maxLevel);
    }

    public List<LessonContent> getLessonContentByDurationRange(Integer minDuration, Integer maxDuration) {
        return lessonContentRepository.findByDurationRange(minDuration, maxDuration);
    }

    public List<LessonContent> getLessonContentByStudentAndDurationRange(Long studentId, Integer minDuration, Integer maxDuration) {
        return lessonContentRepository.findByStudentIdAndDurationRange(studentId, minDuration, maxDuration);
    }

    public List<LessonContent> getLessonContentByCompletionDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return lessonContentRepository.findByCompletionDateRange(startDate, endDate);
    }

    public List<LessonContent> getLessonContentByStudentAndCompletionDateRange(Long studentId, LocalDateTime startDate, LocalDateTime endDate) {
        return lessonContentRepository.findByStudentIdAndCompletionDateRange(studentId, startDate, endDate);
    }

    // Search operations
    public List<LessonContent> searchLessonContent(String searchTerm) {
        return lessonContentRepository.searchByTitleOrDescription(searchTerm);
    }

    public List<LessonContent> searchLessonContentByStudent(Long studentId, String searchTerm) {
        return lessonContentRepository.searchByStudentAndTitleOrDescription(studentId, searchTerm);
    }

    // Progress tracking operations
    public List<LessonContent> getUncompletedLessonContentByStudent(Long studentId) {
        return lessonContentRepository.findUncompletedByStudent(studentId);
    }

    public List<LessonContent> getRecentlyCompletedLessonContent() {
        return lessonContentRepository.findRecentlyCompleted();
    }

    public List<LessonContent> getRecentlyCompletedLessonContentByStudent(Long studentId) {
        return lessonContentRepository.findRecentlyCompletedByStudent(studentId);
    }

    public List<LessonContent> getLessonContentByCreationDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return lessonContentRepository.findByCreationDateRange(startDate, endDate);
    }

    public List<LessonContent> getLessonContentByStudentAndCreationDateRange(Long studentId, LocalDateTime startDate, LocalDateTime endDate) {
        return lessonContentRepository.findByStudentIdAndCreationDateRange(studentId, startDate, endDate);
    }

    // Completion operations
    public LessonContent markAsCompleted(Long id) {
        LessonContent lessonContent = getLessonContentById(id);
        lessonContent.markAsCompleted();
        return lessonContentRepository.save(lessonContent);
    }

    public LessonContent markAsIncomplete(Long id) {
        LessonContent lessonContent = getLessonContentById(id);
        lessonContent.markAsIncomplete();
        return lessonContentRepository.save(lessonContent);
    }

    // Statistics operations
    public Object[] getStudentCompletionStats(Long studentId) {
        return lessonContentRepository.getStudentCompletionStats(studentId);
    }

    public Object[] getOverallCompletionStats() {
        return lessonContentRepository.getOverallCompletionStats();
    }

    public List<Object[]> countByContentType() {
        return lessonContentRepository.countByContentType();
    }

    public List<Object[]> countByDifficultyLevel() {
        return lessonContentRepository.countByDifficultyLevel();
    }

    public List<Object[]> countByStudent() {
        return lessonContentRepository.countByStudent();
    }

    public List<Object[]> countByCompletionStatus() {
        return lessonContentRepository.countByCompletionStatus();
    }

    // Utility methods
    public boolean lessonContentExists(Long id) {
        return lessonContentRepository.existsById(id);
    }

    public long getTotalLessonContentCount() {
        return lessonContentRepository.count();
    }

    public long getCompletedLessonContentCount() {
        return lessonContentRepository.findByCompleted(true).size();
    }

    public long getIncompleteLessonContentCount() {
        return lessonContentRepository.findByCompleted(false).size();
    }

    public double getCompletionRate() {
        Object[] stats = getOverallCompletionStats();
        if (stats != null && stats.length >= 2) {
            Long total = (Long) stats[0];
            Long completed = (Long) stats[1];
            if (total > 0) {
                return (double) completed / total * 100;
            }
        }
        return 0.0;
    }

    public double getStudentCompletionRate(Long studentId) {
        Object[] stats = getStudentCompletionStats(studentId);
        if (stats != null && stats.length >= 2) {
            Long total = (Long) stats[0];
            Long completed = (Long) stats[1];
            if (total > 0) {
                return (double) completed / total * 100;
            }
        }
        return 0.0;
    }
}
