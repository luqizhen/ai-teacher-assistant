package com.pianoteacher.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class ScheduleDTO {

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Start time is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;

    @NotBlank(message = "Location is required")
    @Size(min = 1, max = 100, message = "Location must be between 1 and 100 characters")
    private String location;

    private String notes;

    // Constructors
    public ScheduleDTO() {
    }

    public ScheduleDTO(Long studentId, LocalDateTime startTime, LocalDateTime endTime, String location) {
        this.studentId = studentId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
    }

    // Getters and Setters
    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "ScheduleDTO{" +
                "studentId=" + studentId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", location='" + location + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}
