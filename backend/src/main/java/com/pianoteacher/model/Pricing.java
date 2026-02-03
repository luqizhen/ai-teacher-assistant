package com.pianoteacher.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "pricing")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Pricing extends BaseEntity {

    @NotNull(message = "Hourly rate is required")
    @DecimalMin(value = "0.01", message = "Hourly rate must be positive")
    @Column(name = "hourly_rate", nullable = false, precision = 10, scale = 2)
    private BigDecimal hourlyRate;

    @NotNull(message = "Lesson duration is required")
    @Positive(message = "Lesson duration must be positive")
    @Column(name = "lesson_duration", nullable = false)
    private Integer lessonDuration; // in minutes

    @Column(name = "payment_terms", length = 100)
    private String paymentTerms;

    // Constructors
    public Pricing() {
        this.lessonDuration = 60; // default 1 hour
        this.paymentTerms = "Per lesson";
    }

    public Pricing(BigDecimal hourlyRate, Integer lessonDuration) {
        this();
        this.hourlyRate = hourlyRate;
        this.lessonDuration = lessonDuration;
    }

    // Getters and Setters
    public BigDecimal getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(BigDecimal hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public Integer getLessonDuration() {
        return lessonDuration;
    }

    public void setLessonDuration(Integer lessonDuration) {
        this.lessonDuration = lessonDuration;
    }

    public String getPaymentTerms() {
        return paymentTerms;
    }

    public void setPaymentTerms(String paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    // Business methods
    public void validate() {
        if (hourlyRate == null || hourlyRate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Hourly rate must be positive");
        }
        if (lessonDuration == null || lessonDuration <= 0) {
            throw new IllegalArgumentException("Lesson duration must be positive");
        }
    }

    public BigDecimal calculateLessonCost() {
        validate();
        // Calculate cost based on hourly rate and lesson duration
        return hourlyRate.multiply(BigDecimal.valueOf(lessonDuration))
                         .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
    }

    @Override
    public String toString() {
        return "Pricing{" +
                "id=" + getId() +
                ", hourlyRate=" + hourlyRate +
                ", lessonDuration=" + lessonDuration +
                ", paymentTerms='" + paymentTerms + '\'' +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Pricing pricing = (Pricing) o;

        if (getId() != null ? !getId().equals(pricing.getId()) : pricing.getId() != null) return false;
        if (hourlyRate != null ? !hourlyRate.equals(pricing.hourlyRate) : pricing.hourlyRate != null) return false;
        if (lessonDuration != null ? !lessonDuration.equals(pricing.lessonDuration) : pricing.lessonDuration != null) return false;
        return paymentTerms != null ? paymentTerms.equals(pricing.paymentTerms) : pricing.paymentTerms == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getId() != null ? getId().hashCode() : 0);
        result = 31 * result + (hourlyRate != null ? hourlyRate.hashCode() : 0);
        result = 31 * result + (lessonDuration != null ? lessonDuration.hashCode() : 0);
        result = 31 * result + (paymentTerms != null ? paymentTerms.hashCode() : 0);
        return result;
    }
}
