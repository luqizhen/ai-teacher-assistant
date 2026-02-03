package com.pianoteacher.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "progress_reports")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ProgressReport extends BaseEntity {

    @NotNull(message = "Student is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @NotBlank(message = "Report type is required")
    @Column(name = "report_type", nullable = false, length = 20)
    private String reportType;

    @NotBlank(message = "Report period is required")
    @Length(max = 20, message = "Report period must be at most 20 characters")
    @Column(name = "report_period", nullable = false, length = 20)
    private String reportPeriod;

    @NotNull(message = "Overall progress is required")
    @DecimalMin(value = "0.0", message = "Overall progress must be between 0 and 100")
    @DecimalMax(value = "100.0", message = "Overall progress must be between 0 and 100")
    @Column(name = "overall_progress", nullable = false)
    private Double overallProgress;

    @DecimalMin(value = "0.0", message = "Technical skills must be between 0 and 100")
    @DecimalMax(value = "100.0", message = "Technical skills must be between 0 and 100")
    @Column(name = "technical_skills")
    private Double technicalSkills;

    @DecimalMin(value = "0.0", message = "Theory knowledge must be between 0 and 100")
    @DecimalMax(value = "100.0", message = "Theory knowledge must be between 0 and 100")
    @Column(name = "theory_knowledge")
    private Double theoryKnowledge;

    @DecimalMin(value = "0.0", message = "Repertoire skills must be between 0 and 100")
    @DecimalMax(value = "100.0", message = "Repertoire skills must be between 0 and 100")
    @Column(name = "repertoire_skills")
    private Double repertoireSkills;

    @DecimalMin(value = "0.0", message = "Practice habits must be between 0 and 100")
    @DecimalMax(value = "100.0", message = "Practice habits must be between 0 and 100")
    @Column(name = "practice_habits")
    private Double practiceHabits;

    @Lob
    @Column(name = "strengths")
    private String strengths;

    @Lob
    @Column(name = "areas_for_improvement")
    private String areasForImprovement;

    @Lob
    @Column(name = "recommendations")
    private String recommendations;

    @Lob
    @Column(name = "next_goals")
    private String nextGoals;

    @Lob
    @Column(name = "teacher_notes")
    private String teacherNotes;

    @Column(name = "report_date")
    private LocalDateTime reportDate;

    // Constructors
    public ProgressReport() {
    }

    public ProgressReport(Student student, String reportType, String reportPeriod, Double overallProgress) {
        this.student = student;
        this.reportType = reportType;
        this.reportPeriod = reportPeriod;
        this.overallProgress = overallProgress;
        this.reportDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
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

    // Business methods
    public void validate() {
        if (student == null) {
            throw new IllegalArgumentException("Student is required");
        }
        if (reportType == null || reportType.trim().isEmpty()) {
            throw new IllegalArgumentException("Report type is required");
        }
        if (!isValidReportType(reportType)) {
            throw new IllegalArgumentException("Invalid report type. Must be one of: " + Arrays.toString(ReportType.values()));
        }
        if (reportPeriod == null || reportPeriod.trim().isEmpty()) {
            throw new IllegalArgumentException("Report period is required");
        }
        if (overallProgress == null || overallProgress < 0 || overallProgress > 100) {
            throw new IllegalArgumentException("Overall progress must be between 0 and 100");
        }
        if (technicalSkills != null && (technicalSkills < 0 || technicalSkills > 100)) {
            throw new IllegalArgumentException("Technical skills must be between 0 and 100");
        }
        if (theoryKnowledge != null && (theoryKnowledge < 0 || theoryKnowledge > 100)) {
            throw new IllegalArgumentException("Theory knowledge must be between 0 and 100");
        }
        if (repertoireSkills != null && (repertoireSkills < 0 || repertoireSkills > 100)) {
            throw new IllegalArgumentException("Repertoire skills must be between 0 and 100");
        }
        if (practiceHabits != null && (practiceHabits < 0 || practiceHabits > 100)) {
            throw new IllegalArgumentException("Practice habits must be between 0 and 100");
        }
    }

    private boolean isValidReportType(String reportType) {
        return Arrays.stream(ReportType.values())
                .anyMatch(type -> type.name().equals(reportType));
    }

    public String getProgressGrade() {
        if (overallProgress == null) return "N/A";
        if (overallProgress >= 95) return "A+";
        if (overallProgress >= 90) return "A";
        if (overallProgress >= 85) return "B+";
        if (overallProgress >= 80) return "B";
        if (overallProgress >= 75) return "C+";
        if (overallProgress >= 70) return "C";
        if (overallProgress >= 60) return "D";
        return "F";
    }

    public String getPerformanceLevel() {
        if (overallProgress == null) return "N/A";
        if (overallProgress >= 90) return "Excellent";
        if (overallProgress >= 80) return "Good";
        if (overallProgress >= 70) return "Satisfactory";
        if (overallProgress >= 60) return "Needs Improvement";
        return "Poor";
    }

    public Double getAverageScore() {
        int count = 0;
        double sum = 0.0;
        
        if (technicalSkills != null) {
            sum += technicalSkills;
            count++;
        }
        if (theoryKnowledge != null) {
            sum += theoryKnowledge;
            count++;
        }
        if (repertoireSkills != null) {
            sum += repertoireSkills;
            count++;
        }
        if (practiceHabits != null) {
            sum += practiceHabits;
            count++;
        }
        
        return count > 0 ? sum / count : 0.0;
    }

    public Map<String, Object> getProgressSummary() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("overallProgress", overallProgress);
        summary.put("grade", getProgressGrade());
        summary.put("performanceLevel", getPerformanceLevel());
        summary.put("technicalSkills", technicalSkills);
        summary.put("theoryKnowledge", theoryKnowledge);
        summary.put("repertoireSkills", repertoireSkills);
        summary.put("practiceHabits", practiceHabits);
        summary.put("averageScore", getAverageScore());
        summary.put("reportDate", reportDate);
        return summary;
    }

    @Override
    public String toString() {
        return "ProgressReport{" +
                "id=" + getId() +
                ", student=" + (student != null ? student.getName() : "null") +
                ", reportType='" + reportType + '\'' +
                ", reportPeriod='" + reportPeriod + '\'' +
                ", overallProgress=" + overallProgress +
                ", grade='" + getProgressGrade() + '\'' +
                ", reportDate=" + reportDate +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProgressReport that = (ProgressReport) o;

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

    // Report type enum
    public enum ReportType {
        WEEKLY("Weekly"),
        MONTHLY("Monthly"),
        QUARTERLY("Quarterly"),
        SEMESTER("Semester"),
        YEARLY("Yearly"),
        ASSESSMENT("Assessment");

        private final String displayName;

        ReportType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
