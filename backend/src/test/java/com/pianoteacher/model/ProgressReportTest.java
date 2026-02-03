package com.pianoteacher.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

class ProgressReportTest {

    private ProgressReport progressReport;
    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);
        student.setName("John Doe");
        
        progressReport = new ProgressReport();
        progressReport.setStudent(student);
        progressReport.setReportType("MONTHLY");
        progressReport.setReportPeriod("2024-01");
        progressReport.setOverallProgress(85.5);
        progressReport.setTechnicalSkills(90.0);
        progressReport.setTheoryKnowledge(75.0);
        progressReport.setRepertoireSkills(80.0);
        progressReport.setPracticeHabits(88.0);
        progressReport.setStrengths("Good finger dexterity, consistent practice");
        progressReport.setAreasForImprovement("Sight reading, dynamics control");
        progressReport.setRecommendations("Focus on scale practice and dynamics exercises");
        progressReport.setNextGoals("Complete Grade 3 pieces, improve sight reading");
        progressReport.setTeacherNotes("Showing excellent progress, maintain current practice routine");
    }

    @Test
    void testProgressReportCreation() {
        assertNotNull(progressReport);
        assertEquals(student, progressReport.getStudent());
        assertEquals("MONTHLY", progressReport.getReportType());
        assertEquals("2024-01", progressReport.getReportPeriod());
        assertEquals(85.5, progressReport.getOverallProgress());
        assertEquals(90.0, progressReport.getTechnicalSkills());
        assertEquals(75.0, progressReport.getTheoryKnowledge());
        assertEquals(80.0, progressReport.getRepertoireSkills());
        assertEquals(88.0, progressReport.getPracticeHabits());
        assertEquals("Good finger dexterity, consistent practice", progressReport.getStrengths());
        assertEquals("Sight reading, dynamics control", progressReport.getAreasForImprovement());
        assertEquals("Focus on scale practice and dynamics exercises", progressReport.getRecommendations());
        assertEquals("Complete Grade 3 pieces, improve sight reading", progressReport.getNextGoals());
        assertEquals("Showing excellent progress, maintain current practice routine", progressReport.getTeacherNotes());
    }

    @Test
    void testProgressReportWithNullStudent() {
        progressReport.setStudent(null);
        assertThrows(IllegalArgumentException.class, () -> progressReport.validate());
    }

    @Test
    void testProgressReportWithNullReportType() {
        progressReport.setReportType(null);
        assertThrows(IllegalArgumentException.class, () -> progressReport.validate());
    }

    @Test
    void testProgressReportWithInvalidReportType() {
        progressReport.setReportType("INVALID");
        assertThrows(IllegalArgumentException.class, () -> progressReport.validate());
    }

    @Test
    void testProgressReportWithNullReportPeriod() {
        progressReport.setReportPeriod(null);
        assertThrows(IllegalArgumentException.class, () -> progressReport.validate());
    }

    @Test
    void testProgressReportWithInvalidProgressScores() {
        progressReport.setOverallProgress(-5.0);
        assertThrows(IllegalArgumentException.class, () -> progressReport.validate());
        
        progressReport.setOverallProgress(105.0);
        assertThrows(IllegalArgumentException.class, () -> progressReport.validate());
        
        progressReport.setOverallProgress(85.5); // Reset to valid
        progressReport.setTechnicalSkills(-10.0);
        assertThrows(IllegalArgumentException.class, () -> progressReport.validate());
    }

    @Test
    void testProgressReportToString() {
        String result = progressReport.toString();
        assertTrue(result.contains("MONTHLY"));
        assertTrue(result.contains("2024-01"));
        assertTrue(result.contains("85.5"));
        assertTrue(result.contains("John Doe"));
    }

    @Test
    void testProgressReportEquals() {
        ProgressReport anotherReport = new ProgressReport();
        anotherReport.setStudent(student);
        anotherReport.setReportType("MONTHLY");
        anotherReport.setReportPeriod("2024-01");
        anotherReport.setOverallProgress(85.5);
        anotherReport.setTechnicalSkills(90.0);
        anotherReport.setTheoryKnowledge(75.0);
        anotherReport.setRepertoireSkills(80.0);
        anotherReport.setPracticeHabits(88.0);
        
        // Different IDs, so not equal
        assertNotEquals(progressReport, anotherReport);
        
        // Same ID, so equal
        progressReport.setId(1L);
        anotherReport.setId(1L);
        assertEquals(progressReport, anotherReport);
    }

    @Test
    void testProgressReportHashCode() {
        progressReport.setId(1L);
        ProgressReport anotherReport = new ProgressReport();
        anotherReport.setId(1L);
        anotherReport.setStudent(student);
        anotherReport.setReportType("MONTHLY");
        anotherReport.setReportPeriod("2024-01");
        anotherReport.setOverallProgress(85.5);
        
        assertEquals(progressReport.hashCode(), anotherReport.hashCode());
    }

    @Test
    void testAuditFields() {
        LocalDateTime beforeCreate = LocalDateTime.now();
        progressReport.onCreate();
        LocalDateTime afterCreate = LocalDateTime.now();
        
        assertNotNull(progressReport.getCreatedAt());
        assertTrue(progressReport.getCreatedAt().isAfter(beforeCreate) || progressReport.getCreatedAt().isEqual(beforeCreate));
        assertTrue(progressReport.getCreatedAt().isBefore(afterCreate) || progressReport.getCreatedAt().isEqual(afterCreate));
        
        LocalDateTime beforeUpdate = LocalDateTime.now();
        progressReport.onUpdate();
        LocalDateTime afterUpdate = LocalDateTime.now();
        
        assertNotNull(progressReport.getUpdatedAt());
        assertTrue(progressReport.getUpdatedAt().isAfter(beforeUpdate) || progressReport.getUpdatedAt().isEqual(beforeUpdate));
        assertTrue(progressReport.getUpdatedAt().isBefore(afterUpdate) || progressReport.getUpdatedAt().isEqual(afterUpdate));
    }

    @Test
    void testReportTypeValidation() {
        // Valid report types
        String[] validTypes = {"WEEKLY", "MONTHLY", "QUARTERLY", "SEMESTER", "YEARLY", "ASSESSMENT"};
        for (String type : validTypes) {
            progressReport.setReportType(type);
            assertDoesNotThrow(() -> progressReport.validate());
        }
        
        // Invalid report types
        String[] invalidTypes = {"invalid", "DAILY", "", null};
        for (String type : invalidTypes) {
            progressReport.setReportType(type);
            assertThrows(IllegalArgumentException.class, () -> progressReport.validate());
        }
    }

    @Test
    void testProgressScoreValidation() {
        // Valid progress scores (0-100)
        Double[] validScores = {0.0, 50.5, 100.0};
        for (Double score : validScores) {
            progressReport.setOverallProgress(score);
            progressReport.setTechnicalSkills(score);
            progressReport.setTheoryKnowledge(score);
            progressReport.setRepertoireSkills(score);
            progressReport.setPracticeHabits(score);
            assertDoesNotThrow(() -> progressReport.validate());
        }
        
        // Invalid progress scores
        Double[] invalidScores = {-1.0, -0.1, 100.1, 150.0};
        for (Double score : invalidScores) {
            progressReport.setOverallProgress(score);
            assertThrows(IllegalArgumentException.class, () -> progressReport.validate());
        }
    }

    @Test
    void testGetProgressGrade() {
        progressReport.setOverallProgress(95.0);
        assertEquals("A+", progressReport.getProgressGrade());
        
        progressReport.setOverallProgress(90.0);
        assertEquals("A", progressReport.getProgressGrade());
        
        progressReport.setOverallProgress(85.0);
        assertEquals("B+", progressReport.getProgressGrade());
        
        progressReport.setOverallProgress(80.0);
        assertEquals("B", progressReport.getProgressGrade());
        
        progressReport.setOverallProgress(75.0);
        assertEquals("C+", progressReport.getProgressGrade());
        
        progressReport.setOverallProgress(70.0);
        assertEquals("C", progressReport.getProgressGrade());
        
        progressReport.setOverallProgress(65.0);
        assertEquals("D", progressReport.getProgressGrade());
        
        progressReport.setOverallProgress(60.0);
        assertEquals("D", progressReport.getProgressGrade());
        
        progressReport.setOverallProgress(55.0);
        assertEquals("F", progressReport.getProgressGrade());
    }

    @Test
    void testGetAverageScore() {
        progressReport.setTechnicalSkills(90.0);
        progressReport.setTheoryKnowledge(75.0);
        progressReport.setRepertoireSkills(80.0);
        progressReport.setPracticeHabits(88.0);
        
        double expected = (90.0 + 75.0 + 80.0 + 88.0) / 4.0;
        assertEquals(expected, progressReport.getAverageScore(), 0.001);
    }

    @Test
    void testGetProgressSummary() {
        progressReport.setOverallProgress(85.5);
        progressReport.setTechnicalSkills(90.0);
        progressReport.setTheoryKnowledge(75.0);
        progressReport.setRepertoireSkills(80.0);
        progressReport.setPracticeHabits(88.0);
        
        Map<String, Object> summary = progressReport.getProgressSummary();
        assertNotNull(summary);
        assertEquals(85.5, summary.get("overallProgress"));
        assertEquals("B+", summary.get("grade"));
        assertEquals(90.0, summary.get("technicalSkills"));
        assertEquals(75.0, summary.get("theoryKnowledge"));
        assertEquals(80.0, summary.get("repertoireSkills"));
        assertEquals(88.0, summary.get("practiceHabits"));
        assertEquals(83.25, summary.get("averageScore"));
    }

    @Test
    void testGetPerformanceLevel() {
        progressReport.setOverallProgress(95.0);
        assertEquals("Excellent", progressReport.getPerformanceLevel());
        
        progressReport.setOverallProgress(85.0);
        assertEquals("Good", progressReport.getPerformanceLevel());
        
        progressReport.setOverallProgress(75.0);
        assertEquals("Satisfactory", progressReport.getPerformanceLevel());
        
        progressReport.setOverallProgress(65.0);
        assertEquals("Needs Improvement", progressReport.getPerformanceLevel());
        
        progressReport.setOverallProgress(55.0);
        assertEquals("Poor", progressReport.getPerformanceLevel());
    }
}
