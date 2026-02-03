package com.pianoteacher.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.Duration;

class ScheduleTest {

    private Schedule schedule;
    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);
        student.setName("John Doe");
        student.setAge(15);
        
        schedule = new Schedule();
        schedule.setStudent(student);
        schedule.setStartTime(LocalDateTime.of(2024, 1, 15, 14, 0));
        schedule.setEndTime(LocalDateTime.of(2024, 1, 15, 14, 30));
        schedule.setLocation("Studio A");
        schedule.setNotes("Regular lesson");
    }

    @Test
    void testScheduleCreation() {
        assertNotNull(schedule);
        assertEquals(student, schedule.getStudent());
        assertEquals(LocalDateTime.of(2024, 1, 15, 14, 0), schedule.getStartTime());
        assertEquals(LocalDateTime.of(2024, 1, 15, 14, 30), schedule.getEndTime());
        assertEquals("Studio A", schedule.getLocation());
        assertEquals("Regular lesson", schedule.getNotes());
    }

    @Test
    void testScheduleWithNullStudent() {
        schedule.setStudent(null);
        assertThrows(IllegalArgumentException.class, () -> schedule.validate());
    }

    @Test
    void testScheduleWithNullStartTime() {
        schedule.setStartTime(null);
        assertThrows(IllegalArgumentException.class, () -> schedule.validate());
    }

    @Test
    void testScheduleWithNullEndTime() {
        schedule.setEndTime(null);
        assertThrows(IllegalArgumentException.class, () -> schedule.validate());
    }

    @Test
    void testScheduleWithEndTimeBeforeStartTime() {
        schedule.setEndTime(LocalDateTime.of(2024, 1, 15, 13, 0));
        assertThrows(IllegalArgumentException.class, () -> schedule.validate());
    }

    @Test
    void testScheduleWithEmptyLocation() {
        schedule.setLocation("");
        assertThrows(IllegalArgumentException.class, () -> schedule.validate());
    }

    @Test
    void testScheduleDuration() {
        Duration duration = schedule.getDuration();
        assertEquals(Duration.ofMinutes(30), duration);
    }

    @Test
    void testScheduleDurationInHours() {
        assertEquals(0.5, schedule.getDurationInHours(), 0.001);
    }

    @Test
    void testScheduleWithOneHourDuration() {
        schedule.setEndTime(LocalDateTime.of(2024, 1, 15, 15, 0));
        assertEquals(1.0, schedule.getDurationInHours(), 0.001);
    }

    @Test
    void testScheduleStatus() {
        // Test future schedule
        Schedule futureSchedule = new Schedule();
        futureSchedule.setStartTime(LocalDateTime.now().plusHours(1));
        futureSchedule.setEndTime(LocalDateTime.now().plusHours(2));
        assertEquals("SCHEDULED", futureSchedule.getStatus());
        
        // Test past schedule
        Schedule pastSchedule = new Schedule();
        pastSchedule.setStartTime(LocalDateTime.now().minusHours(2));
        pastSchedule.setEndTime(LocalDateTime.now().minusHours(1));
        assertEquals("COMPLETED", pastSchedule.getStatus());
        
        // Test ongoing schedule
        Schedule ongoingSchedule = new Schedule();
        ongoingSchedule.setStartTime(LocalDateTime.now().minusMinutes(30));
        ongoingSchedule.setEndTime(LocalDateTime.now().plusMinutes(30));
        assertEquals("IN_PROGRESS", ongoingSchedule.getStatus());
    }

    @Test
    void testScheduleToString() {
        String result = schedule.toString();
        assertTrue(result.contains("John Doe"));
        assertTrue(result.contains("Studio A"));
        assertTrue(result.contains("2024-01-15T14:00"));
    }

    @Test
    void testScheduleEquals() {
        Schedule anotherSchedule = new Schedule();
        anotherSchedule.setStudent(student);
        anotherSchedule.setStartTime(LocalDateTime.of(2024, 1, 15, 14, 0));
        anotherSchedule.setEndTime(LocalDateTime.of(2024, 1, 15, 14, 30));
        anotherSchedule.setLocation("Studio A");
        anotherSchedule.setNotes("Regular lesson");
        
        // Different IDs, so not equal
        assertNotEquals(schedule, anotherSchedule);
        
        // Same ID, so equal
        schedule.setId(1L);
        anotherSchedule.setId(1L);
        assertEquals(schedule, anotherSchedule);
    }

    @Test
    void testScheduleHashCode() {
        schedule.setId(1L);
        Schedule anotherSchedule = new Schedule();
        anotherSchedule.setId(1L);
        anotherSchedule.setStudent(student);
        anotherSchedule.setStartTime(LocalDateTime.of(2024, 1, 15, 14, 0));
        anotherSchedule.setEndTime(LocalDateTime.of(2024, 1, 15, 14, 30));
        anotherSchedule.setLocation("Studio A");
        anotherSchedule.setNotes("Regular lesson");
        
        assertEquals(schedule.hashCode(), anotherSchedule.hashCode());
    }

    @Test
    void testAuditFields() {
        LocalDateTime beforeCreate = LocalDateTime.now();
        schedule.onCreate();
        LocalDateTime afterCreate = LocalDateTime.now();
        
        assertNotNull(schedule.getCreatedAt());
        assertTrue(schedule.getCreatedAt().isAfter(beforeCreate) || schedule.getCreatedAt().isEqual(beforeCreate));
        assertTrue(schedule.getCreatedAt().isBefore(afterCreate) || schedule.getCreatedAt().isEqual(afterCreate));
        
        LocalDateTime beforeUpdate = LocalDateTime.now();
        schedule.onUpdate();
        LocalDateTime afterUpdate = LocalDateTime.now();
        
        assertNotNull(schedule.getUpdatedAt());
        assertTrue(schedule.getUpdatedAt().isAfter(beforeUpdate) || schedule.getUpdatedAt().isEqual(beforeUpdate));
        assertTrue(schedule.getUpdatedAt().isBefore(afterUpdate) || schedule.getUpdatedAt().isEqual(afterUpdate));
    }
}
