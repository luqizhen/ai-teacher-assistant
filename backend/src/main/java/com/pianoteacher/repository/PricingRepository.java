package com.pianoteacher.repository;

import com.pianoteacher.model.Pricing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PricingRepository extends JpaRepository<Pricing, Long> {

    // Find by hourly rate range
    List<Pricing> findByHourlyRateBetween(BigDecimal minRate, BigDecimal maxRate);

    // Find by lesson duration
    List<Pricing> findByLessonDuration(Integer lessonDuration);

    // Find by payment terms
    List<Pricing> findByPaymentTerms(String paymentTerms);

    // Find by payment terms (case-insensitive)
    List<Pricing> findByPaymentTermsIgnoreCase(String paymentTerms);

    // Custom query to find pricing by hourly rate
    @Query("SELECT p FROM Pricing p WHERE p.hourlyRate = :rate")
    List<Pricing> findByHourlyRate(@Param("rate") BigDecimal rate);

    // Custom query to find most common lesson duration
    @Query("SELECT p.lessonDuration, COUNT(p) as count FROM Pricing p GROUP BY p.lessonDuration ORDER BY count DESC")
    List<Object[]> findMostCommonLessonDurations();

    // Custom query to find average hourly rate
    @Query("SELECT AVG(p.hourlyRate) FROM Pricing p")
    BigDecimal findAverageHourlyRate();

    // Custom query to find pricing by payment terms containing specific text
    @Query("SELECT p FROM Pricing p WHERE LOWER(p.paymentTerms) LIKE LOWER(CONCAT('%', :terms, '%'))")
    List<Pricing> findByPaymentTermsContaining(@Param("terms") String terms);

    // Count pricing by lesson duration
    @Query("SELECT p.lessonDuration, COUNT(p) FROM Pricing p GROUP BY p.lessonDuration")
    List<Object[]> countPricingByLessonDuration();

    // Find pricing created within a date range
    @Query("SELECT p FROM Pricing p WHERE p.createdAt BETWEEN :startDate AND :endDate")
    List<Pricing> findPricingByDateRange(@Param("startDate") java.time.LocalDateTime startDate,
                                        @Param("endDate") java.time.LocalDateTime endDate);
}
