package com.pianoteacher.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class TimeSlotSuggestionDTO {

    @NotNull(message = "Start time is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;

    @NotNull(message = "Confidence score is required")
    private Double confidence; // 0.0 to 1.0, higher means more confident

    private String reason; // Explanation for why this time is suggested

    private String studentName;

    private Integer duration; // Duration in minutes

    // Constructors
    public TimeSlotSuggestionDTO() {
    }

    public TimeSlotSuggestionDTO(LocalDateTime startTime, LocalDateTime endTime, Double confidence, String reason) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.confidence = confidence;
        this.reason = reason;
        this.duration = (int) java.time.Duration.between(startTime, endTime).toMinutes();
    }

    // Getters and Setters
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

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "TimeSlotSuggestionDTO{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", confidence=" + confidence +
                ", reason='" + reason + '\'' +
                ", studentName='" + studentName + '\'' +
                ", duration=" + duration +
                '}';
    }
}
