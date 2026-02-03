package com.pianoteacher.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

class LessonContentTest {

    private LessonContent lessonContent;
    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);
        student.setName("John Doe");
        
        lessonContent = new LessonContent();
        lessonContent.setStudent(student);
        lessonContent.setTitle("Scales Practice");
        lessonContent.setDescription("Major and minor scales practice");
        lessonContent.setContentType("EXERCISE");
        lessonContent.setDifficultyLevel(3);
        lessonContent.setEstimatedDuration(30);
        lessonContent.setNotes("Focus on C major scale");
    }

    @Test
    void testLessonContentCreation() {
        assertNotNull(lessonContent);
        assertEquals(student, lessonContent.getStudent());
        assertEquals("Scales Practice", lessonContent.getTitle());
        assertEquals("Major and minor scales practice", lessonContent.getDescription());
        assertEquals("EXERCISE", lessonContent.getContentType());
        assertEquals(Integer.valueOf(3), lessonContent.getDifficultyLevel());
        assertEquals(Integer.valueOf(30), lessonContent.getEstimatedDuration());
        assertEquals("Focus on C major scale", lessonContent.getNotes());
    }

    @Test
    void testLessonContentWithNullStudent() {
        lessonContent.setStudent(null);
        assertThrows(IllegalArgumentException.class, () -> lessonContent.validate());
    }

    @Test
    void testLessonContentWithNullTitle() {
        lessonContent.setTitle(null);
        assertThrows(IllegalArgumentException.class, () -> lessonContent.validate());
    }

    @Test
    void testLessonContentWithEmptyTitle() {
        lessonContent.setTitle("");
        assertThrows(IllegalArgumentException.class, () -> lessonContent.validate());
    }

    @Test
    void testLessonContentWithInvalidContentType() {
        lessonContent.setContentType("INVALID");
        assertThrows(IllegalArgumentException.class, () -> lessonContent.validate());
    }

    @Test
    void testLessonContentWithInvalidDifficultyLevel() {
        lessonContent.setDifficultyLevel(0);
        assertThrows(IllegalArgumentException.class, () -> lessonContent.validate());
        
        lessonContent.setDifficultyLevel(11);
        assertThrows(IllegalArgumentException.class, () -> lessonContent.validate());
    }

    @Test
    void testLessonContentWithInvalidDuration() {
        lessonContent.setEstimatedDuration(0);
        assertThrows(IllegalArgumentException.class, () -> lessonContent.validate());
        
        lessonContent.setEstimatedDuration(-5);
        assertThrows(IllegalArgumentException.class, () -> lessonContent.validate());
    }

    @Test
    void testLessonContentToString() {
        String result = lessonContent.toString();
        assertTrue(result.contains("Scales Practice"));
        assertTrue(result.contains("EXERCISE"));
        assertTrue(result.contains("John Doe"));
    }

    @Test
    void testLessonContentEquals() {
        LessonContent anotherContent = new LessonContent();
        anotherContent.setStudent(student);
        anotherContent.setTitle("Scales Practice");
        anotherContent.setDescription("Major and minor scales practice");
        anotherContent.setContentType("EXERCISE");
        anotherContent.setDifficultyLevel(3);
        anotherContent.setEstimatedDuration(30);
        anotherContent.setNotes("Focus on C major scale");
        
        // Different IDs, so not equal
        assertNotEquals(lessonContent, anotherContent);
        
        // Same ID, so equal
        lessonContent.setId(1L);
        anotherContent.setId(1L);
        assertEquals(lessonContent, anotherContent);
    }

    @Test
    void testLessonContentHashCode() {
        lessonContent.setId(1L);
        LessonContent anotherContent = new LessonContent();
        anotherContent.setId(1L);
        anotherContent.setStudent(student);
        anotherContent.setTitle("Scales Practice");
        anotherContent.setDescription("Major and minor scales practice");
        anotherContent.setContentType("EXERCISE");
        anotherContent.setDifficultyLevel(3);
        anotherContent.setEstimatedDuration(30);
        anotherContent.setNotes("Focus on C major scale");
        
        assertEquals(lessonContent.hashCode(), anotherContent.hashCode());
    }

    @Test
    void testAuditFields() {
        LocalDateTime beforeCreate = LocalDateTime.now();
        lessonContent.onCreate();
        LocalDateTime afterCreate = LocalDateTime.now();
        
        assertNotNull(lessonContent.getCreatedAt());
        assertTrue(lessonContent.getCreatedAt().isAfter(beforeCreate) || lessonContent.getCreatedAt().isEqual(beforeCreate));
        assertTrue(lessonContent.getCreatedAt().isBefore(afterCreate) || lessonContent.getCreatedAt().isEqual(afterCreate));
        
        LocalDateTime beforeUpdate = LocalDateTime.now();
        lessonContent.onUpdate();
        LocalDateTime afterUpdate = LocalDateTime.now();
        
        assertNotNull(lessonContent.getUpdatedAt());
        assertTrue(lessonContent.getUpdatedAt().isAfter(beforeUpdate) || lessonContent.getUpdatedAt().isEqual(beforeUpdate));
        assertTrue(lessonContent.getUpdatedAt().isBefore(afterUpdate) || lessonContent.getUpdatedAt().isEqual(afterUpdate));
    }

    @Test
    void testContentTypeValidation() {
        // Valid content types
        String[] validTypes = {"EXERCISE", "SONG", "THEORY", "TECHNIQUE", "REPERTOIRE", "ASSIGNMENT"};
        for (String type : validTypes) {
            lessonContent.setContentType(type);
            assertDoesNotThrow(() -> lessonContent.validate());
        }
        
        // Invalid content types
        String[] invalidTypes = {"invalid", "EXERCISES", "", null};
        for (String type : invalidTypes) {
            lessonContent.setContentType(type);
            assertThrows(IllegalArgumentException.class, () -> lessonContent.validate());
        }
    }

    @Test
    void testDifficultyLevelValidation() {
        // Valid difficulty levels (1-10)
        for (int level = 1; level <= 10; level++) {
            lessonContent.setDifficultyLevel(level);
            assertDoesNotThrow(() -> lessonContent.validate());
        }
        
        // Invalid difficulty levels
        int[] invalidLevels = {0, -1, 11, 100};
        for (int level : invalidLevels) {
            lessonContent.setDifficultyLevel(level);
            assertThrows(IllegalArgumentException.class, () -> lessonContent.validate());
        }
    }
}
