package com.pianoteacher.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "schedules", indexes = {
    @Index(name = "idx_schedule_student_id", columnList = "student_id"),
    @Index(name = "idx_schedule_start_time", columnList = "start_time"),
    @Index(name = "idx_schedule_end_time", columnList = "end_time"),
    @Index(name = "idx_schedule_student_time", columnList = "student_id, start_time")
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Schedule extends BaseEntity {

    @NotNull(message = "Student is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @NotNull(message = "Start time is required")
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @NotBlank(message = "Location is required")
    @Length(min = 1, max = 100, message = "Location must be between 1 and 100 characters")
    @Column(name = "location", nullable = false, length = 100)
    private String location;

    @Lob
    @Column(name = "notes")
    private String notes;

    // Constructors
    public Schedule() {
    }

    public Schedule(Student student, LocalDateTime startTime, LocalDateTime endTime, String location) {
        this.student = student;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
    }

    // Getters and Setters
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
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

    // Business methods
    public void validate() {
        if (student == null) {
            throw new IllegalArgumentException("Student is required");
        }
        if (startTime == null) {
            throw new IllegalArgumentException("Start time is required");
        }
        if (endTime == null) {
            throw new IllegalArgumentException("End time is required");
        }
        if (endTime.isBefore(startTime) || endTime.isEqual(startTime)) {
            throw new IllegalArgumentException("End time must be after start time");
        }
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("Location is required");
        }
    }

    public Duration getDuration() {
        return Duration.between(startTime, endTime);
    }

    public double getDurationInHours() {
        return getDuration().toMinutes() / 60.0;
    }

    public String getStatus() {
        LocalDateTime now = LocalDateTime.now();
        
        if (now.isBefore(startTime)) {
            return "SCHEDULED";
        } else if (now.isAfter(endTime)) {
            return "COMPLETED";
        } else {
            return "IN_PROGRESS";
        }
    }

    public boolean isOverlapping(Schedule other) {
        if (other == null || !student.equals(other.student)) {
            return false;
        }
        
        return startTime.isBefore(other.getEndTime()) && endTime.isAfter(other.getStartTime());
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + getId() +
                ", student=" + (student != null ? student.getName() : "null") +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", location='" + location + '\'' +
                ", status='" + getStatus() + '\'' +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Schedule schedule = (Schedule) o;

        // Standard JPA entity equality: use ID if both have IDs
        if (getId() != null && schedule.getId() != null) {
            return getId().equals(schedule.getId());
        }
        
        // If one has ID and other doesn't, they're not equal
        if (getId() != null || schedule.getId() != null) {
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
}
