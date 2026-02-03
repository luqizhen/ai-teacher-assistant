package com.pianoteacher.controller;

import com.pianoteacher.model.Student;
import com.pianoteacher.model.Pricing;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
@Tag(name = "Student Management", description = "Operations for managing student information and pricing")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Student CRUD endpoints
    @PostMapping
    @Operation(
            summary = "Create a new student",
            description = "Creates a new student record with the provided information. The student must have a valid name, age, and other required fields."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Student created successfully", 
                    content = @Content(schema = @Schema(implementation = Student.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Student> createStudent(
            @Parameter(description = "Student object to be created", required = true)
            @Valid @RequestBody Student student) {
        try {
            Student createdStudent = studentService.createStudent(student);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get student by ID",
            description = "Retrieves a student record using their unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student found", 
                    content = @Content(schema = @Schema(implementation = Student.class))),
            @ApiResponse(responseCode = "404", description = "Student not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Student> getStudentById(
            @Parameter(description = "ID of the student to retrieve", required = true)
            @PathVariable Long id) {
        try {
            Student student = studentService.getStudentById(id);
            return ResponseEntity.ok(student);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(
            summary = "Get all students",
            description = "Retrieves a list of all students in the system."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of students retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @Valid @RequestBody Student studentDetails) {
        try {
            Student updatedStudent = studentService.updateStudent(id, studentDetails);
            return ResponseEntity.ok(updatedStudent);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        try {
            studentService.deleteStudent(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Search and filter endpoints
    @GetMapping("/search")
    public ResponseEntity<List<Student>> searchStudents(@RequestParam String term) {
        List<Student> students = studentService.searchStudents(term);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<Student>> searchStudentsByName(@RequestParam String name) {
        List<Student> students = studentService.searchStudentsByName(name);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/filter/age")
    public ResponseEntity<List<Student>> getStudentsByAgeRange(
            @RequestParam Integer minAge,
            @RequestParam Integer maxAge) {
        List<Student> students = studentService.getStudentsByAgeRange(minAge, maxAge);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/filter/grade")
    public ResponseEntity<List<Student>> getStudentsByGrade(@RequestParam String grade) {
        List<Student> students = studentService.getStudentsByGrade(grade);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/filter/with-pricing")
    public ResponseEntity<List<Student>> getStudentsWithPricing() {
        List<Student> students = studentService.getStudentsWithPricing();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/filter/without-pricing")
    public ResponseEntity<List<Student>> getStudentsWithoutPricing() {
        List<Student> students = studentService.getStudentsWithoutPricing();
        return ResponseEntity.ok(students);
    }

    // Pricing management endpoints
    @PostMapping("/{id}/pricing")
    public ResponseEntity<Student> addPricingToStudent(@PathVariable Long id, @Valid @RequestBody Pricing pricing) {
        try {
            Student student = studentService.addPricingToStudent(id, pricing);
            return ResponseEntity.ok(student);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}/pricing")
    public ResponseEntity<Student> removePricingFromStudent(@PathVariable Long id) {
        try {
            Student student = studentService.removePricingFromStudent(id);
            return ResponseEntity.ok(student);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Statistics and reporting endpoints
    @GetMapping("/stats/grade-counts")
    public ResponseEntity<List<Map<String, Object>>> getStudentCountByGrade() {
        List<Object[]> results = studentService.getStudentCountByGrade();
        List<Map<String, Object>> stats = results.stream()
                .map(result -> Map.of(
                        "grade", result[0],
                        "count", result[1]
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/average-hourly-rate")
    public ResponseEntity<Map<String, Object>> getAverageHourlyRate() {
        java.math.BigDecimal averageRate = studentService.getAverageHourlyRate();
        Map<String, Object> result = Map.of(
                "averageHourlyRate", averageRate != null ? averageRate : 0
        );
        return ResponseEntity.ok(result);
    }

    @GetMapping("/stats/common-lesson-durations")
    public ResponseEntity<List<Map<String, Object>>> getMostCommonLessonDurations() {
        List<Object[]> results = studentService.getMostCommonLessonDurations();
        List<Map<String, Object>> stats = results.stream()
                .map(result -> Map.of(
                        "lessonDuration", result[0],
                        "count", result[1]
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(stats);
    }

    // Utility endpoints
    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> checkStudentExists(@PathVariable Long id) {
        boolean exists = studentService.studentExists(id);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam String email) {
        return studentService.getStudentByEmail(email)
                .map(student -> ResponseEntity.ok(true))
                .orElse(ResponseEntity.ok(false));
    }

    @GetMapping("/check-phone")
    public ResponseEntity<Boolean> checkPhoneExists(@RequestParam String phone) {
        return studentService.getStudentByPhone(phone)
                .map(student -> ResponseEntity.ok(true))
                .orElse(ResponseEntity.ok(false));
    }

    @GetMapping("/filter/date-range")
    public ResponseEntity<List<Student>> getStudentsCreatedInDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            LocalDateTime start = LocalDateTime.parse(startDate);
            LocalDateTime end = LocalDateTime.parse(endDate);
            List<Student> students = studentService.getStudentsCreatedInDateRange(start, end);
            return ResponseEntity.ok(students);
        } catch (Exception e) {
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
