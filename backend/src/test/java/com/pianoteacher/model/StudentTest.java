package com.pianoteacher.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

class StudentTest {

    private Student student;
    private Pricing pricing;

    @BeforeEach
    void setUp() {
        pricing = new Pricing();
        pricing.setHourlyRate(new BigDecimal("50.0"));
        pricing.setLessonDuration(60);
        pricing.setPaymentTerms("Monthly");
        
        student = new Student();
        student.setName("John Doe");
        student.setAge(15);
        student.setGrade("Grade 10");
        student.setEmail("john.doe@example.com");
        student.setPhone("555-123-4567");
        student.setNotes("Interested in classical music");
        // Don't set pricing here to avoid test complexity
    }

    @Test
    void testStudentCreation() {
        assertNotNull(student);
        assertEquals("John Doe", student.getName());
        assertEquals(15, student.getAge());
        assertEquals("Grade 10", student.getGrade());
        assertEquals("john.doe@example.com", student.getEmail());
        assertEquals("555-123-4567", student.getPhone());
        assertEquals("Interested in classical music", student.getNotes());
        assertNull(student.getPricing()); // Should be null initially
    }

    @Test
    void testStudentWithNullName() {
        student.setName(null);
        assertThrows(IllegalArgumentException.class, () -> student.validate());
    }

    @Test
    void testStudentWithEmptyName() {
        student.setName("");
        assertThrows(IllegalArgumentException.class, () -> student.validate());
    }

    @Test
    void testStudentWithInvalidAge() {
        student.setAge(-1);
        assertThrows(IllegalArgumentException.class, () -> student.validate());
        
        student.setAge(101);
        assertThrows(IllegalArgumentException.class, () -> student.validate());
    }

    @Test
    void testStudentWithInvalidEmail() {
        student.setEmail("invalid-email");
        assertThrows(IllegalArgumentException.class, () -> student.validate());
    }

    @Test
    void testStudentWithValidAge() {
        student.setAge(5);
        assertDoesNotThrow(() -> student.validate());
        
        student.setAge(100);
        assertDoesNotThrow(() -> student.validate());
    }

    @Test
    void testStudentToString() {
        String result = student.toString();
        assertTrue(result.contains("John Doe"));
        assertTrue(result.contains("15"));
        assertTrue(result.contains("Grade 10"));
    }

    @Test
    void testStudentEquals() {
        // Test basic equality logic
        Student anotherStudent = new Student();
        anotherStudent.setName("John Doe");
        anotherStudent.setAge(15);
        anotherStudent.setGrade("Grade 10");
        anotherStudent.setEmail("john.doe@example.com");
        anotherStudent.setPhone("555-123-4567");
        anotherStudent.setNotes("Interested in classical music");
        
        // Different objects with null IDs should be different instances
        assertNotEquals(student, anotherStudent);
        
        // Same ID, so equal (standard JPA behavior)
        student.setId(1L);
        anotherStudent.setId(1L);
        assertEquals(student, anotherStudent);
        
        // Test null comparison
        assertNotEquals(student, null);
        
        // Test different class comparison
        assertNotEquals(student, "not a student");
        
        // Test different IDs
        anotherStudent.setId(2L);
        assertNotEquals(student, anotherStudent);
        
        // Reset ID for other tests
        anotherStudent.setId(1L);
        assertEquals(student, anotherStudent);
    }

    @Test
    void testStudentHashCode() {
        // Test basic hashCode functionality
        student.setId(1L);
        Student anotherStudent = new Student();
        anotherStudent.setId(1L);
        anotherStudent.setName("John Doe");
        anotherStudent.setAge(15);
        anotherStudent.setGrade("Grade 10");
        anotherStudent.setEmail("john.doe@example.com");
        anotherStudent.setPhone("555-123-4567");
        anotherStudent.setNotes("Interested in classical music");
        
        // Equal objects must have equal hash codes
        assertEquals(student, anotherStudent);
        assertEquals(student.hashCode(), anotherStudent.hashCode());
        
        // Hash code should be consistent
        int hashCode1 = student.hashCode();
        int hashCode2 = student.hashCode();
        assertEquals(hashCode1, hashCode2);
        
        // Hash code should be different for different objects
        Student differentStudent = new Student();
        differentStudent.setId(2L);
        differentStudent.setName("Jane Doe");
        assertNotEquals(student.hashCode(), differentStudent.hashCode());
    }

    @Test
    void testAuditFields() {
        LocalDateTime beforeCreate = LocalDateTime.now();
        student.onCreate();
        LocalDateTime afterCreate = LocalDateTime.now();
        
        assertNotNull(student.getCreatedAt());
        assertTrue(student.getCreatedAt().isAfter(beforeCreate) || student.getCreatedAt().isEqual(beforeCreate));
        assertTrue(student.getCreatedAt().isBefore(afterCreate) || student.getCreatedAt().isEqual(afterCreate));
        
        LocalDateTime beforeUpdate = LocalDateTime.now();
        student.onUpdate();
        LocalDateTime afterUpdate = LocalDateTime.now();
        
        assertNotNull(student.getUpdatedAt());
        assertTrue(student.getUpdatedAt().isAfter(beforeUpdate) || student.getUpdatedAt().isEqual(beforeUpdate));
        assertTrue(student.getUpdatedAt().isBefore(afterUpdate) || student.getUpdatedAt().isEqual(afterUpdate));
    }
}
