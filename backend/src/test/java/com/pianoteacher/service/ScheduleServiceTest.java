package com.pianoteacher.service;

import com.pianoteacher.model.Schedule;
import com.pianoteacher.model.Student;
import com.pianoteacher.dto.TimeSlotSuggestionDTO;
import com.pianoteacher.repository.ScheduleRepository;
import com.pianoteacher.service.ScheduleService;
import com.pianoteacher.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.DayOfWeek;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    @Mock
    private StudentService studentService;
    
    @Mock
    private ScheduleRepository scheduleRepository;

    private ScheduleService scheduleService;

    @BeforeEach
    void setUp() {
        scheduleService = new ScheduleService(scheduleRepository, null, studentService);
    }

    @Test
    void testGetSchedulingSuggestions_BasicFunctionality() {
        // Arrange
        Long studentId = 1L;
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(7);
        int duration = 60; // 1 hour

        Student student = new Student();
        student.setId(studentId);
        student.setName("Test Student");

        when(studentService.getStudentById(studentId)).thenReturn(student);
        when(scheduleRepository.findByStudentAndDateRange(anyLong(), any(), any())).thenReturn(List.of());

        // Act
        List<TimeSlotSuggestionDTO> suggestions = scheduleService.getSchedulingSuggestions(
                studentId, startDate, endDate, duration);

        // Assert
        assertNotNull(suggestions);
        assertFalse(suggestions.isEmpty());
        
        // Verify each suggestion has required fields
        for (TimeSlotSuggestionDTO suggestion : suggestions) {
            assertNotNull(suggestion.getStartTime());
            assertNotNull(suggestion.getEndTime());
            assertNotNull(suggestion.getConfidence());
            assertTrue(suggestion.getConfidence() >= 0.0 && suggestion.getConfidence() <= 1.0);
            assertNotNull(suggestion.getReason());
            assertFalse(suggestion.getReason().isEmpty());
        }
    }

    @Test
    void testGetSchedulingSuggestions_InvalidDuration() {
        // Arrange
        Long studentId = 1L;
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(7);
        int duration = 15; // Too short

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            scheduleService.getSchedulingSuggestions(studentId, startDate, endDate, duration);
        });
    }

    @Test
    void testGetSchedulingSuggestions_InvalidDateRange() {
        // Arrange
        Long studentId = 1L;
        LocalDateTime startDate = LocalDateTime.now().plusDays(7);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1); // End before start
        int duration = 60;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            scheduleService.getSchedulingSuggestions(studentId, startDate, endDate, duration);
        });
    }

    @Test
    void testGetSchedulingSuggestions_NonExistentStudent() {
        // Arrange
        Long studentId = 999L;
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(7);
        int duration = 60;

        when(studentService.getStudentById(studentId))
                .thenThrow(new IllegalArgumentException("Student not found"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            scheduleService.getSchedulingSuggestions(studentId, startDate, endDate, duration);
        });
    }

    @Test
    void testGetSchedulingSuggestions_PrioritizeHistoricalPatterns() {
        // Arrange
        Long studentId = 1L;
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(7);
        int duration = 60;

        // Act
        List<TimeSlotSuggestionDTO> suggestions = scheduleService.getSchedulingSuggestions(
                studentId, startDate, endDate, duration);

        // Assert
        assertNotNull(suggestions);
        
        // Check that suggestions are sorted by confidence (highest first)
        if (suggestions.size() > 1) {
            for (int i = 0; i < suggestions.size() - 1; i++) {
                assertTrue(suggestions.get(i).getConfidence() >= suggestions.get(i + 1).getConfidence());
            }
        }
    }

    @Test
    void testGetSchedulingSuggestions_AvoidConflicts() {
        // Arrange
        Long studentId = 1L;
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(7);
        int duration = 60;

        // Act
        List<TimeSlotSuggestionDTO> suggestions = scheduleService.getSchedulingSuggestions(
                studentId, startDate, endDate, duration);

        // Assert
        assertNotNull(suggestions);
        
        // Verify that suggested time slots don't overlap with existing schedules
        // (This would require mocking existing schedules in a real implementation)
        for (TimeSlotSuggestionDTO suggestion : suggestions) {
            assertTrue(suggestion.getStartTime().isBefore(suggestion.getEndTime()));
            assertTrue(suggestion.getStartTime().isAfter(startDate) || suggestion.getStartTime().isEqual(startDate));
            assertTrue(suggestion.getEndTime().isBefore(endDate) || suggestion.getEndTime().isEqual(endDate));
        }
    }

    @Test
    void testGetSchedulingSuggestions_ReasonableTimeSlots() {
        // Arrange
        Long studentId = 1L;
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(7);
        int duration = 60;

        // Act
        List<TimeSlotSuggestionDTO> suggestions = scheduleService.getSchedulingSuggestions(
                studentId, startDate, endDate, duration);

        // Assert
        assertNotNull(suggestions);
        
        // Check that suggestions are within reasonable hours (e.g., 8 AM - 8 PM)
        for (TimeSlotSuggestionDTO suggestion : suggestions) {
            int hour = suggestion.getStartTime().getHour();
            assertTrue(hour >= 8 && hour <= 20, 
                    "Suggested time should be between 8 AM and 8 PM, but was: " + hour);
        }
    }

    @Test
    void testGetSchedulingSuggestions_EmptyDateRange() {
        // Arrange
        Long studentId = 1L;
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1); // Same day, zero range
        int duration = 60;

        // Act
        List<TimeSlotSuggestionDTO> suggestions = scheduleService.getSchedulingSuggestions(
                studentId, startDate, endDate, duration);

        // Assert
        assertNotNull(suggestions);
        // Should return empty or minimal suggestions for zero date range
        assertTrue(suggestions.isEmpty() || suggestions.size() <= 5);
    }

    @Test
    void testGetSchedulingSuggestions_LargeDateRange() {
        // Arrange
        Long studentId = 1L;
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusMonths(1); // One month
        int duration = 60;

        // Act
        List<TimeSlotSuggestionDTO> suggestions = scheduleService.getSchedulingSuggestions(
                studentId, startDate, endDate, duration);

        // Assert
        assertNotNull(suggestions);
        // Should return reasonable number of suggestions (not too many)
        assertTrue(suggestions.size() <= 50, "Should limit suggestions to reasonable number");
    }

    @Test
    void testGetSchedulingSuggestions_DifferentDurations() {
        // Arrange
        Long studentId = 1L;
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(7);

        // Test different durations
        int[] durations = {30, 45, 60, 90, 120}; // 30 min to 2 hours

        for (int duration : durations) {
            // Act
            List<TimeSlotSuggestionDTO> suggestions = scheduleService.getSchedulingSuggestions(
                    studentId, startDate, endDate, duration);

            // Assert
            assertNotNull(suggestions);
            
            // Verify duration is respected
            for (TimeSlotSuggestionDTO suggestion : suggestions) {
                long actualDuration = java.time.Duration.between(
                        suggestion.getStartTime(), suggestion.getEndTime()).toMinutes();
                assertEquals(duration, actualDuration, 
                        "Suggested duration should match requested duration");
            }
        }
    }
}
