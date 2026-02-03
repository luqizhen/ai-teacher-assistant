package com.pianoteacher.controller;

import com.pianoteacher.dto.ScheduleDTO;
import com.pianoteacher.dto.TimeSlotSuggestionDTO;
import com.pianoteacher.model.Schedule;
import com.pianoteacher.model.Student;
import com.pianoteacher.service.ScheduleService;
import com.pianoteacher.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/schedules")
@CrossOrigin(origins = "*")
@Tag(name = "Schedule Management", description = "Operations for managing teaching schedules and availability")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final StudentService studentService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService, StudentService studentService) {
        this.scheduleService = scheduleService;
        this.studentService = studentService;
    }

    // Schedule CRUD endpoints
    @PostMapping
    public ResponseEntity<Schedule> createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        try {
            // Convert DTO to entity
            Schedule schedule = new Schedule();
            Student student = studentService.getStudentById(scheduleDTO.getStudentId());
            schedule.setStudent(student);
            schedule.setStartTime(scheduleDTO.getStartTime());
            schedule.setEndTime(scheduleDTO.getEndTime());
            schedule.setLocation(scheduleDTO.getLocation());
            schedule.setNotes(scheduleDTO.getNotes());
            
            Schedule createdSchedule = scheduleService.createSchedule(schedule);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSchedule);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Schedule> getScheduleById(@PathVariable Long id) {
        try {
            Schedule schedule = scheduleService.getScheduleById(id);
            return ResponseEntity.ok(schedule);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Schedule>> getAllSchedules() {
        List<Schedule> schedules = scheduleService.getAllSchedules();
        return ResponseEntity.ok(schedules);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Schedule> updateSchedule(@PathVariable Long id, @Valid @RequestBody Schedule scheduleDetails) {
        try {
            Schedule updatedSchedule = scheduleService.updateSchedule(id, scheduleDetails);
            return ResponseEntity.ok(updatedSchedule);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        try {
            scheduleService.deleteSchedule(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Schedule query endpoints
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Schedule>> getSchedulesByStudent(@PathVariable Long studentId) {
        List<Schedule> schedules = scheduleService.getSchedulesByStudent(studentId);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Schedule>> getSchedulesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<Schedule> schedules = scheduleService.getSchedulesByDateRange(startDate, endDate);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/student/{studentId}/date-range")
    public ResponseEntity<List<Schedule>> getSchedulesByStudentAndDateRange(
            @PathVariable Long studentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<Schedule> schedules = scheduleService.getSchedulesByStudentAndDateRange(studentId, startDate, endDate);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/location")
    public ResponseEntity<List<Schedule>> getSchedulesByLocation(@RequestParam String location) {
        List<Schedule> schedules = scheduleService.getSchedulesByLocation(location);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/date")
    public ResponseEntity<List<Schedule>> getSchedulesByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        List<Schedule> schedules = scheduleService.getSchedulesByDate(date);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/student/{studentId}/date")
    public ResponseEntity<List<Schedule>> getSchedulesByStudentAndDate(
            @PathVariable Long studentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        List<Schedule> schedules = scheduleService.getSchedulesByStudentAndDate(studentId, date);
        return ResponseEntity.ok(schedules);
    }

    // Schedule status endpoints
    @GetMapping("/upcoming")
    public ResponseEntity<List<Schedule>> getUpcomingSchedules() {
        List<Schedule> schedules = scheduleService.getUpcomingSchedules();
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/ongoing")
    public ResponseEntity<List<Schedule>> getOngoingSchedules() {
        List<Schedule> schedules = scheduleService.getOngoingSchedules();
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/completed")
    public ResponseEntity<List<Schedule>> getCompletedSchedules() {
        List<Schedule> schedules = scheduleService.getCompletedSchedules();
        return ResponseEntity.ok(schedules);
    }

    // Advanced query endpoints
    @GetMapping("/next-days/{days}")
    public ResponseEntity<List<Schedule>> getSchedulesInNextDays(@PathVariable int days) {
        List<Schedule> schedules = scheduleService.getSchedulesInNextDays(days);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/student/{studentId}/next-days/{days}")
    public ResponseEntity<List<Schedule>> getStudentSchedulesInNextDays(@PathVariable Long studentId, @PathVariable int days) {
        List<Schedule> schedules = scheduleService.getStudentSchedulesInNextDays(studentId, days);
        return ResponseEntity.ok(schedules);
    }

    // Utility endpoints
    @GetMapping("/check-availability/{studentId}")
    public ResponseEntity<Boolean> checkStudentAvailability(
            @PathVariable Long studentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        boolean available = scheduleService.isStudentAvailable(studentId, startTime, endTime);
        return ResponseEntity.ok(available);
    }

    @GetMapping("/check-conflict/{studentId}")
    public ResponseEntity<Boolean> checkScheduleConflict(
            @PathVariable Long studentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        boolean hasConflict = scheduleService.hasScheduleConflict(studentId, startTime, endTime);
        return ResponseEntity.ok(hasConflict);
    }

    @GetMapping("/conflicts/{studentId}")
    public ResponseEntity<List<Schedule>> findConflictingSchedules(
            @PathVariable Long studentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        List<Schedule> conflicts = scheduleService.findConflictingSchedules(studentId, startTime, endTime);
        return ResponseEntity.ok(conflicts);
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> checkScheduleExists(@PathVariable Long id) {
        boolean exists = scheduleService.scheduleExists(id);
        return ResponseEntity.ok(exists);
    }

    // Schedule management endpoints
    @PutMapping("/{id}/reschedule")
    public ResponseEntity<Schedule> rescheduleSchedule(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime newStartTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime newEndTime) {
        try {
            Schedule rescheduled = scheduleService.rescheduleSchedule(id, newStartTime, newEndTime);
            return ResponseEntity.ok(rescheduled);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/location")
    public ResponseEntity<Schedule> changeScheduleLocation(@PathVariable Long id, @RequestParam String newLocation) {
        try {
            Schedule updated = scheduleService.changeScheduleLocation(id, newLocation);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/notes")
    public ResponseEntity<Schedule> updateScheduleNotes(@PathVariable Long id, @RequestParam String notes) {
        Schedule updated = scheduleService.updateScheduleNotes(id, notes);
        return ResponseEntity.ok(updated);
    }

    // Statistics endpoints
    @GetMapping("/stats/student-counts")
    public ResponseEntity<List<Map<String, Object>>> getScheduleCountsByStudent() {
        List<Object[]> results = scheduleService.countSchedulesByStudent();
        List<Map<String, Object>> stats = results.stream()
                .map(result -> Map.of(
                        "studentId", result[0],
                        "scheduleCount", result[1]
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/location-counts")
    public ResponseEntity<List<Map<String, Object>>> getScheduleCountsByLocation() {
        List<Object[]> results = scheduleService.countSchedulesByLocation();
        List<Map<String, Object>> stats = results.stream()
                .map(result -> Map.of(
                        "location", result[0],
                        "scheduleCount", result[1]
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(stats);
    }

    // Scheduling suggestions endpoint
    @GetMapping("/suggestions")
    @Operation(
            summary = "Get intelligent scheduling suggestions",
            description = "Provides AI-powered scheduling suggestions based on student's historical patterns, availability, and preferences. " +
                    "The algorithm analyzes past scheduling data to suggest optimal time slots with confidence scores."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Scheduling suggestions generated successfully", 
                    content = @Content(schema = @Schema(implementation = TimeSlotSuggestionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid parameters or date range"),
            @ApiResponse(responseCode = "404", description = "Student not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<TimeSlotSuggestionDTO>> getSchedulingSuggestions(
            @Parameter(description = "ID of the student for whom to generate suggestions", required = true, example = "1")
            @RequestParam Long studentId,
            @Parameter(description = "Start date for scheduling suggestions", required = true, example = "2025-02-01T00:00:00")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date for scheduling suggestions", required = true, example = "2025-02-07T23:59:59")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @Parameter(description = "Duration of the lesson in minutes (30-240)", required = true, example = "60")
            @RequestParam int duration) {
        try {
            List<TimeSlotSuggestionDTO> suggestions = scheduleService.getSchedulingSuggestions(studentId, startDate, endDate, duration);
            return ResponseEntity.ok(suggestions);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Error handling example
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException e) {
        Map<String, String> error = Map.of(
                "error", "Validation Error",
                "message", e.getMessage(),
                "timestamp", LocalDateTime.now().toString()
        );
        return ResponseEntity.badRequest().body(error);
    }
}
