package com.pianoteacher.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class ProgressReportDTO {

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotBlank(message = "Report type is required")
    @Size(min = 1, max = 20, message = "Report type must be between 1 and 20 characters")
    private String reportType;

    @NotBlank(message = "Report period is required")
    @Size(min = 1, max = 20, message = "Report period must be between 1 and 20 characters")
    private String reportPeriod;

    @NotNull(message = "Overall progress is required")
    @DecimalMin(value = "0.0", message = "Overall progress must be between 0 and 100")
    @DecimalMax(value = "100.0", message = "Overall progress must be between 0 and 100")
    private Double overallProgress;

    @DecimalMin(value = "0.0", message = "Technical skills must be between 0 and 100")
    @DecimalMax(value = "100.0", message = "Technical skills must be between 0 and 100")
    private Double technicalSkills;

    @DecimalMin(value = "0.0", message = "Theory knowledge must be between 0 and 100")
    @DecimalMax(value = "100.0", message = "Theory knowledge must be between 0 and 100")
    private Double theoryKnowledge;

    @DecimalMin(value = "0.0", message = "Repertoire skills must be between 0 and 100")
    @DecimalMax(value = "100.0", message = "Repertoire skills must be between 0 and 100")
    private Double repertoireSkills;

    @DecimalMin(value = "0.0", message = "Practice habits must be between 0 and 100")
    @DecimalMax(value = "100.0", message = "Practice habits must be between 0 and 100")
    private Double practiceHabits;

    private String strengths;

    private String areasForImprovement;

    private String recommendations;

    private String nextGoals;

    private String teacherNotes;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime reportDate;

    // Constructors
    public ProgressReportDTO() {
    }

    public ProgressReportDTO(Long studentId, String reportType, String reportPeriod, Double overallProgress) {
        this.studentId = studentId;
        this.reportType = reportType;
        this.reportPeriod = reportPeriod;
        this.overallProgress = overallProgress;
    }

    // Getters and Setters
    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getReportPeriod() {
        return reportPeriod;
    }

    public void setReportPeriod(String reportPeriod) {
        this.reportPeriod = reportPeriod;
    }

    public Double getOverallProgress() {
        return overallProgress;
    }

    public void setOverallProgress(Double overallProgress) {
        this.overallProgress = overallProgress;
    }

    public Double getTechnicalSkills() {
        return technicalSkills;
    }

    public void setTechnicalSkills(Double technicalSkills) {
        this.technicalSkills = technicalSkills;
    }

    public Double getTheoryKnowledge() {
        return theoryKnowledge;
    }

    public void setTheoryKnowledge(Double theoryKnowledge) {
        this.theoryKnowledge = theoryKnowledge;
    }

    public Double getRepertoireSkills() {
        return repertoireSkills;
    }

    public void setRepertoireSkills(Double repertoireSkills) {
        this.repertoireSkills = repertoireSkills;
    }

    public Double getPracticeHabits() {
        return practiceHabits;
    }

    public void setPracticeHabits(Double practiceHabits) {
        this.practiceHabits = practiceHabits;
    }

    public String getStrengths() {
        return strengths;
    }

    public void setStrengths(String strengths) {
        this.strengths = strengths;
    }

    public String getAreasForImprovement() {
        return areasForImprovement;
    }

    public void setAreasForImprovement(String areasForImprovement) {
        this.areasForImprovement = areasForImprovement;
    }

    public String getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(String recommendations) {
        this.recommendations = recommendations;
    }

    public String getNextGoals() {
        return nextGoals;
    }

    public void setNextGoals(String nextGoals) {
        this.nextGoals = nextGoals;
    }

    public String getTeacherNotes() {
        return teacherNotes;
    }

    public void setTeacherNotes(String teacherNotes) {
        this.teacherNotes = teacherNotes;
    }

    public LocalDateTime getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDateTime reportDate) {
        this.reportDate = reportDate;
    }

    @Override
    public String toString() {
        return "ProgressReportDTO{" +
                "studentId=" + studentId +
                ", reportType='" + reportType + '\'' +
                ", reportPeriod='" + reportPeriod + '\'' +
                ", overallProgress=" + overallProgress +
                ", technicalSkills=" + technicalSkills +
                ", theoryKnowledge=" + theoryKnowledge +
                ", repertoireSkills=" + repertoireSkills +
                ", practiceHabits=" + practiceHabits +
                ", reportDate=" + reportDate +
                '}';
    }
}
