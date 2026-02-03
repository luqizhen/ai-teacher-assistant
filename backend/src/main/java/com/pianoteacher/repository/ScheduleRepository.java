package com.pianoteacher.repository;

import com.pianoteacher.model.Schedule;
import com.pianoteacher.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    // Find schedules by student
    List<Schedule> findByStudent(Student student);
    List<Schedule> findByStudentId(Long studentId);

    // Find schedules by date range
    @Query("SELECT s FROM Schedule s WHERE s.startTime >= :startDate AND s.startTime <= :endDate")
    List<Schedule> findByDateRange(@Param("startDate") LocalDateTime startDate, 
                                   @Param("endDate") LocalDateTime endDate);

    // Find schedules by student and date range
    @Query("SELECT s FROM Schedule s WHERE s.student.id = :studentId AND s.startTime >= :startDate AND s.startTime <= :endDate")
    List<Schedule> findByStudentAndDateRange(@Param("studentId") Long studentId,
                                             @Param("startDate") LocalDateTime startDate,
                                             @Param("endDate") LocalDateTime endDate);

    // Find schedules by location
    List<Schedule> findByLocation(String location);
    List<Schedule> findByLocationContainingIgnoreCase(String location);

    // Find schedules by status (calculated based on current time)
    @Query("SELECT s FROM Schedule s WHERE s.startTime > :now")
    List<Schedule> findUpcomingSchedules(@Param("now") LocalDateTime now);

    @Query("SELECT s FROM Schedule s WHERE s.startTime <= :now AND s.endTime >= :now")
    List<Schedule> findOngoingSchedules(@Param("now") LocalDateTime now);

    @Query("SELECT s FROM Schedule s WHERE s.endTime < :now")
    List<Schedule> findCompletedSchedules(@Param("now") LocalDateTime now);

    // Find overlapping schedules for a student
    @Query("SELECT s FROM Schedule s WHERE s.student.id = :studentId AND " +
           "((s.startTime < :endTime AND s.endTime > :startTime))")
    List<Schedule> findOverlappingSchedules(@Param("studentId") Long studentId,
                                           @Param("startTime") LocalDateTime startTime,
                                           @Param("endTime") LocalDateTime endTime);

    // Count schedules by student
    @Query("SELECT s.student.id, COUNT(s) FROM Schedule s GROUP BY s.student.id")
    List<Object[]> countSchedulesByStudent();

    // Count schedules by location
    @Query("SELECT s.location, COUNT(s) FROM Schedule s GROUP BY s.location")
    List<Object[]> countSchedulesByLocation();

    // Find schedules in the next N days
    @Query("SELECT s FROM Schedule s WHERE s.startTime >= :now AND s.startTime <= :futureDate")
    List<Schedule> findSchedulesInNextDays(@Param("now") LocalDateTime now,
                                          @Param("futureDate") LocalDateTime futureDate);

    // Find schedules by student in the next N days
    @Query("SELECT s FROM Schedule s WHERE s.student.id = :studentId AND s.startTime >= :now AND s.startTime <= :futureDate")
    List<Schedule> findStudentSchedulesInNextDays(@Param("studentId") Long studentId,
                                                  @Param("now") LocalDateTime now,
                                                  @Param("futureDate") LocalDateTime futureDate);

    // Check if student has schedule at specific time
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Schedule s " +
           "WHERE s.student.id = :studentId AND " +
           "((s.startTime <= :startTime AND s.endTime > :startTime) OR " +
           "(s.startTime < :endTime AND s.endTime >= :endTime) OR " +
           "(s.startTime >= :startTime AND s.endTime <= :endTime))")
    boolean isStudentAvailable(@Param("studentId") Long studentId,
                              @Param("startTime") LocalDateTime startTime,
                              @Param("endTime") LocalDateTime endTime);

    // Find schedules created within a date range
    @Query("SELECT s FROM Schedule s WHERE s.createdAt BETWEEN :startDate AND :endDate")
    List<Schedule> findSchedulesByCreationDateRange(@Param("startDate") LocalDateTime startDate,
                                                   @Param("endDate") LocalDateTime endDate);
}
