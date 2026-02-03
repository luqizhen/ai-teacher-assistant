package com.pianoteacher.service;

import com.pianoteacher.model.Schedule;
import com.pianoteacher.model.Student;
import com.pianoteacher.dto.TimeSlotSuggestionDTO;
import com.pianoteacher.repository.ScheduleRepository;
import com.pianoteacher.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final StudentRepository studentRepository;
    private final StudentService studentService;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository, StudentRepository studentRepository, StudentService studentService) {
        this.scheduleRepository = scheduleRepository;
        this.studentRepository = studentRepository;
        this.studentService = studentService;
    }

    // Schedule CRUD operations
    public Schedule createSchedule(Schedule schedule) {
        schedule.validate();
        
        // Check if student exists
        if (!studentRepository.existsById(schedule.getStudent().getId())) {
            throw new IllegalArgumentException("Student not found with id: " + schedule.getStudent().getId());
        }
        
        // Check for overlapping schedules
        List<Schedule> overlappingSchedules = scheduleRepository.findOverlappingSchedules(
            schedule.getStudent().getId(),
            schedule.getStartTime(),
            schedule.getEndTime()
        );
        
        if (!overlappingSchedules.isEmpty()) {
            throw new IllegalArgumentException("Schedule conflicts with existing schedule(s)");
        }
        
        return scheduleRepository.save(schedule);
    }

    public Schedule updateSchedule(Long id, Schedule scheduleDetails) {
        Schedule existingSchedule = getScheduleById(id);
        
        // Check if student exists
        if (!studentRepository.existsById(scheduleDetails.getStudent().getId())) {
            throw new IllegalArgumentException("Student not found with id: " + scheduleDetails.getStudent().getId());
        }
        
        // Check for overlapping schedules (excluding current schedule)
        List<Schedule> overlappingSchedules = scheduleRepository.findOverlappingSchedules(
            scheduleDetails.getStudent().getId(),
            scheduleDetails.getStartTime(),
            scheduleDetails.getEndTime()
        );
        
        overlappingSchedules.removeIf(s -> s.getId().equals(id));
        
        if (!overlappingSchedules.isEmpty()) {
            throw new IllegalArgumentException("Schedule conflicts with existing schedule(s)");
        }
        
        // Update schedule details
        existingSchedule.setStudent(scheduleDetails.getStudent());
        existingSchedule.setStartTime(scheduleDetails.getStartTime());
        existingSchedule.setEndTime(scheduleDetails.getEndTime());
        existingSchedule.setLocation(scheduleDetails.getLocation());
        existingSchedule.setNotes(scheduleDetails.getNotes());
        
        existingSchedule.validate();
        return scheduleRepository.save(existingSchedule);
    }

    public void deleteSchedule(Long id) {
        Schedule schedule = getScheduleById(id);
        scheduleRepository.delete(schedule);
    }

    public Schedule getScheduleById(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found with id: " + id));
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    // Schedule query operations
    public List<Schedule> getSchedulesByStudent(Long studentId) {
        return scheduleRepository.findByStudentId(studentId);
    }

    public List<Schedule> getSchedulesByStudent(Student student) {
        return scheduleRepository.findByStudent(student);
    }

    public List<Schedule> getSchedulesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return scheduleRepository.findByDateRange(startDate, endDate);
    }

    public List<Schedule> getSchedulesByStudentAndDateRange(Long studentId, LocalDateTime startDate, LocalDateTime endDate) {
        return scheduleRepository.findByStudentAndDateRange(studentId, startDate, endDate);
    }

    public List<Schedule> getSchedulesByLocation(String location) {
        return scheduleRepository.findByLocationContainingIgnoreCase(location);
    }

    public List<Schedule> getSchedulesByDate(LocalDateTime date) {
        // Simplified implementation - find all schedules and filter by date
        return scheduleRepository.findAll().stream()
                .filter(s -> s.getStartTime().toLocalDate().equals(date.toLocalDate()))
                .collect(Collectors.toList());
    }

    public List<Schedule> getSchedulesByStudentAndDate(Long studentId, LocalDateTime date) {
        // Simplified implementation - find all schedules and filter
        return scheduleRepository.findByStudentId(studentId).stream()
                .filter(s -> s.getStartTime().toLocalDate().equals(date.toLocalDate()))
                .collect(Collectors.toList());
    }

    // Schedule status operations
    public List<Schedule> getUpcomingSchedules() {
        return scheduleRepository.findUpcomingSchedules(LocalDateTime.now());
    }

    public List<Schedule> getOngoingSchedules() {
        return scheduleRepository.findOngoingSchedules(LocalDateTime.now());
    }

    public List<Schedule> getCompletedSchedules() {
        return scheduleRepository.findCompletedSchedules(LocalDateTime.now());
    }

    // Advanced query operations
    public List<Schedule> getSchedulesInNextDays(int days) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime futureDate = now.plusDays(days);
        return scheduleRepository.findSchedulesInNextDays(now, futureDate);
    }

    public List<Schedule> getStudentSchedulesInNextDays(Long studentId, int days) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime futureDate = now.plusDays(days);
        return scheduleRepository.findStudentSchedulesInNextDays(studentId, now, futureDate);
    }

    // Utility methods
    public boolean isStudentAvailable(Long studentId, LocalDateTime startTime, LocalDateTime endTime) {
        return scheduleRepository.isStudentAvailable(studentId, startTime, endTime);
    }

    public boolean scheduleExists(Long id) {
        return scheduleRepository.existsById(id);
    }

    public List<Object[]> countSchedulesByStudent() {
        return scheduleRepository.countSchedulesByStudent();
    }

    public List<Object[]> countSchedulesByLocation() {
        return scheduleRepository.countSchedulesByLocation();
    }

    // Conflict checking
    public boolean hasScheduleConflict(Long studentId, LocalDateTime startTime, LocalDateTime endTime) {
        List<Schedule> overlappingSchedules = scheduleRepository.findOverlappingSchedules(studentId, startTime, endTime);
        return !overlappingSchedules.isEmpty();
    }

    public List<Schedule> findConflictingSchedules(Long studentId, LocalDateTime startTime, LocalDateTime endTime) {
        return scheduleRepository.findOverlappingSchedules(studentId, startTime, endTime);
    }

    // Schedule management
    public Schedule rescheduleSchedule(Long id, LocalDateTime newStartTime, LocalDateTime newEndTime) {
        Schedule schedule = getScheduleById(id);
        
        // Check for conflicts with new time
        List<Schedule> conflictingSchedules = scheduleRepository.findOverlappingSchedules(
            schedule.getStudent().getId(),
            newStartTime,
            newEndTime
        );
        
        conflictingSchedules.removeIf(s -> s.getId().equals(id));
        
        if (!conflictingSchedules.isEmpty()) {
            throw new IllegalArgumentException("Cannot reschedule: conflicts with existing schedule(s)");
        }
        
        schedule.setStartTime(newStartTime);
        schedule.setEndTime(newEndTime);
        schedule.validate();
        
        return scheduleRepository.save(schedule);
    }

    public Schedule changeScheduleLocation(Long id, String newLocation) {
        Schedule schedule = getScheduleById(id);
        schedule.setLocation(newLocation);
        schedule.validate();
        return scheduleRepository.save(schedule);
    }

    public Schedule updateScheduleNotes(Long id, String notes) {
        Schedule schedule = getScheduleById(id);
        schedule.setNotes(notes);
        return scheduleRepository.save(schedule);
    }

    // Scheduling suggestion algorithm
    public List<TimeSlotSuggestionDTO> getSchedulingSuggestions(Long studentId, LocalDateTime startDate, LocalDateTime endDate, int duration) {
        // Validate inputs
        if (duration < 30 || duration > 240) {
            throw new IllegalArgumentException("Duration must be between 30 and 240 minutes");
        }
        
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
        
        if (startDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Start date cannot be in the past");
        }
        
        // Get student information
        Student student = studentService.getStudentById(studentId);
        
        // Get existing schedules for the student
        List<Schedule> existingSchedules = getSchedulesByStudentAndDateRange(studentId, startDate, endDate);
        
        // Generate potential time slots
        List<TimeSlotSuggestionDTO> suggestions = generateTimeSlots(startDate, endDate, duration, existingSchedules);
        
        // Analyze historical patterns and adjust confidence scores
        suggestions = adjustConfidenceBasedOnPatterns(suggestions, studentId, existingSchedules);
        
        // Sort by confidence score (highest first)
        suggestions.sort((a, b) -> Double.compare(b.getConfidence(), a.getConfidence()));
        
        // Limit to top suggestions
        return suggestions.stream().limit(20).collect(Collectors.toList());
    }
    
    private List<TimeSlotSuggestionDTO> generateTimeSlots(LocalDateTime startDate, LocalDateTime endDate, int duration, List<Schedule> existingSchedules) {
        List<TimeSlotSuggestionDTO> suggestions = new ArrayList<>();
        
        // Define reasonable teaching hours (8 AM - 8 PM)
        LocalTime teachingStart = LocalTime.of(8, 0);
        LocalTime teachingEnd = LocalTime.of(20, 0);
        
        LocalDateTime current = startDate.with(teachingStart);
        
        while (current.isBefore(endDate)) {
            // Skip weekends (Saturday and Sunday)
            if (current.getDayOfWeek() != DayOfWeek.SATURDAY && current.getDayOfWeek() != DayOfWeek.SUNDAY) {
                
                // Generate slots for each hour within teaching hours
                LocalDateTime slotStart = current.with(teachingStart);
                LocalDateTime slotEnd = slotStart.plusMinutes(duration);
                
                while (slotEnd.isBefore(current.with(teachingEnd)) || slotEnd.isEqual(current.with(teachingEnd))) {
                    // Check if this slot conflicts with existing schedules
                    boolean hasConflict = false;
                    for (Schedule existing : existingSchedules) {
                        if (isTimeSlotConflicting(slotStart, slotEnd, existing.getStartTime(), existing.getEndTime())) {
                            hasConflict = true;
                            break;
                        }
                    }
                    
                    if (!hasConflict) {
                        TimeSlotSuggestionDTO suggestion = new TimeSlotSuggestionDTO();
                        suggestion.setStartTime(slotStart);
                        suggestion.setEndTime(slotEnd);
                        suggestion.setDuration(duration);
                        suggestion.setConfidence(0.5); // Base confidence
                        suggestion.setReason("Available time slot with no conflicts");
                        suggestions.add(suggestion);
                    }
                    
                    // Move to next hour
                    slotStart = slotStart.plusHours(1);
                    slotEnd = slotStart.plusMinutes(duration);
                }
            }
            
            // Move to next day
            current = current.plusDays(1);
        }
        
        return suggestions;
    }
    
    private boolean isTimeSlotConflicting(LocalDateTime newStart, LocalDateTime newEnd, LocalDateTime existingStart, LocalDateTime existingEnd) {
        return newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart);
    }
    
    private List<TimeSlotSuggestionDTO> adjustConfidenceBasedOnPatterns(List<TimeSlotSuggestionDTO> suggestions, Long studentId, List<Schedule> existingSchedules) {
        // Analyze historical patterns from existing schedules
        Map<DayOfWeek, Integer> dayFrequency = new HashMap<>();
        Map<Integer, Integer> hourFrequency = new HashMap<>();
        
        for (Schedule schedule : existingSchedules) {
            DayOfWeek day = schedule.getStartTime().getDayOfWeek();
            int hour = schedule.getStartTime().getHour();
            
            dayFrequency.put(day, dayFrequency.getOrDefault(day, 0) + 1);
            hourFrequency.put(hour, hourFrequency.getOrDefault(hour, 0) + 1);
        }
        
        // Adjust confidence based on patterns
        for (TimeSlotSuggestionDTO suggestion : suggestions) {
            DayOfWeek day = suggestion.getStartTime().getDayOfWeek();
            int hour = suggestion.getStartTime().getHour();
            
            // Increase confidence for historically preferred days
            double dayBonus = (dayFrequency.getOrDefault(day, 0) * 0.1);
            
            // Increase confidence for historically preferred hours
            double hourBonus = (hourFrequency.getOrDefault(hour, 0) * 0.1);
            
            // Decrease confidence for very early or very late slots
            if (hour < 10 || hour > 18) {
                suggestion.setConfidence(suggestion.getConfidence() - 0.2);
            } else {
                suggestion.setConfidence(suggestion.getConfidence() + 0.1);
            }
            
            // Apply pattern bonuses
            suggestion.setConfidence(Math.min(1.0, suggestion.getConfidence() + dayBonus + hourBonus));
            
            // Update reason based on patterns
            if (dayFrequency.getOrDefault(day, 0) > 0) {
                String reason = suggestion.getReason();
                reason += " (Matches preferred day pattern)";
                suggestion.setReason(reason);
            }
            
            if (hourFrequency.getOrDefault(hour, 0) > 0) {
                String reason = suggestion.getReason();
                reason += " (Matches preferred time pattern)";
                suggestion.setReason(reason);
            }
            
            // Ensure confidence is within valid range
            suggestion.setConfidence(Math.max(0.0, Math.min(1.0, suggestion.getConfidence())));
        }
        
        return suggestions;
    }
}
