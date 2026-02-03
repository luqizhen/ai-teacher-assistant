package com.pianoteacher.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class LessonContentDTO {

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 200, message = "Title must be between 1 and 200 characters")
    private String title;

    @Size(max = 1000, message = "Description must be at most 1000 characters")
    private String description;

    @NotBlank(message = "Content type is required")
    @Size(min = 1, max = 50, message = "Content type must be between 1 and 50 characters")
    private String contentType;

    @NotNull(message = "Difficulty level is required")
    @Min(value = 1, message = "Difficulty level must be between 1 and 10")
    @Max(value = 10, message = "Difficulty level must be between 1 and 10")
    private Integer difficultyLevel;

    @NotNull(message = "Estimated duration is required")
    @Min(value = 1, message = "Estimated duration must be at least 1 minute")
    private Integer estimatedDuration;

    private String notes;

    private Boolean completed = false;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime completionDate;

    // Constructors
    public LessonContentDTO() {
    }

    public LessonContentDTO(Long studentId, String title, String contentType, Integer difficultyLevel, Integer estimatedDuration) {
        this.studentId = studentId;
        this.title = title;
        this.contentType = contentType;
        this.difficultyLevel = difficultyLevel;
        this.estimatedDuration = estimatedDuration;
    }

    // Getters and Setters
    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
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

    @Override
    public String toString() {
        return "LessonContentDTO{" +
                "studentId=" + studentId +
                ", title='" + title + '\'' +
                ", contentType='" + contentType + '\'' +
                ", difficultyLevel=" + difficultyLevel +
                ", estimatedDuration=" + estimatedDuration +
                ", completed=" + completed +
                ", completionDate=" + completionDate +
                '}';
    }
}
