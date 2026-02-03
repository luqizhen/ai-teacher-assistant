package com.pianoteacher.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "lesson_content")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class LessonContent extends BaseEntity {

    @NotNull(message = "Student is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @NotBlank(message = "Title is required")
    @Length(min = 1, max = 200, message = "Title must be between 1 and 200 characters")
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Length(max = 1000, message = "Description must be at most 1000 characters")
    @Column(name = "description", length = 1000)
    private String description;

    @NotBlank(message = "Content type is required")
    @Column(name = "content_type", nullable = false, length = 50)
    private String contentType;

    @NotNull(message = "Difficulty level is required")
    @Min(value = 1, message = "Difficulty level must be between 1 and 10")
    @Max(value = 10, message = "Difficulty level must be between 1 and 10")
    @Column(name = "difficulty_level", nullable = false)
    private Integer difficultyLevel;

    @NotNull(message = "Estimated duration is required")
    @Min(value = 1, message = "Estimated duration must be at least 1 minute")
    @Column(name = "estimated_duration", nullable = false)
    private Integer estimatedDuration; // in minutes

    @Lob
    @Column(name = "notes")
    private String notes;

    @Column(name = "completed")
    private Boolean completed = false;

    @Column(name = "completion_date")
    private LocalDateTime completionDate;

    // Constructors
    public LessonContent() {
    }

    public LessonContent(Student student, String title, String contentType, Integer difficultyLevel, Integer estimatedDuration) {
        this.student = student;
        this.title = title;
        this.contentType = contentType;
        this.difficultyLevel = difficultyLevel;
        this.estimatedDuration = estimatedDuration;
    }

    // Getters and Setters
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Integer getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(Integer difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public Integer getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(Integer estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public LocalDateTime getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDateTime completionDate) {
        this.completionDate = completionDate;
    }

    // Business methods
    public void validate() {
        if (student == null) {
            throw new IllegalArgumentException("Student is required");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }
        if (contentType == null || contentType.trim().isEmpty()) {
            throw new IllegalArgumentException("Content type is required");
        }
        if (!isValidContentType(contentType)) {
            throw new IllegalArgumentException("Invalid content type. Must be one of: " + Arrays.toString(ContentType.values()));
        }
        if (difficultyLevel == null || difficultyLevel < 1 || difficultyLevel > 10) {
            throw new IllegalArgumentException("Difficulty level must be between 1 and 10");
        }
        if (estimatedDuration == null || estimatedDuration < 1) {
            throw new IllegalArgumentException("Estimated duration must be at least 1 minute");
        }
    }

    private boolean isValidContentType(String contentType) {
        return Arrays.stream(ContentType.values())
                .anyMatch(type -> type.name().equals(contentType));
    }

    public void markAsCompleted() {
        this.completed = true;
        this.completionDate = LocalDateTime.now();
    }

    public void markAsIncomplete() {
        this.completed = false;
        this.completionDate = null;
    }

    public boolean isCompleted() {
        return Boolean.TRUE.equals(completed);
    }

    public String getDifficultyDescription() {
        if (difficultyLevel == null) return "Unknown";
        if (difficultyLevel <= 2) return "Beginner";
        if (difficultyLevel <= 4) return "Elementary";
        if (difficultyLevel <= 6) return "Intermediate";
        if (difficultyLevel <= 8) return "Advanced";
        return "Expert";
    }

    @Override
    public String toString() {
        return "LessonContent{" +
                "id=" + getId() +
                ", student=" + (student != null ? student.getName() : "null") +
                ", title='" + title + '\'' +
                ", contentType='" + contentType + '\'' +
                ", difficultyLevel=" + difficultyLevel +
                ", estimatedDuration=" + estimatedDuration +
                ", completed=" + completed +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LessonContent that = (LessonContent) o;

        // Standard JPA entity equality: use ID if both have IDs
        if (getId() != null && that.getId() != null) {
            return getId().equals(that.getId());
        }
        
        // If one has ID and other doesn't, they're not equal
        if (getId() != null || that.getId() != null) {
            return false;
        }
        
        // Both have null IDs - use identity comparison (different objects are not equal)
        return false;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        return result;
    }

    // Content type enum
    public enum ContentType {
        EXERCISE("Exercise"),
        SONG("Song"),
        THEORY("Theory"),
        TECHNIQUE("Technique"),
        REPERTOIRE("Repertoire"),
        ASSIGNMENT("Assignment");

        private final String displayName;

        ContentType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
