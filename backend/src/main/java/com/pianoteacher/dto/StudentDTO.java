package com.pianoteacher.dto;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.math.BigDecimal;

public class StudentDTO {

    private Long id;

    @NotBlank(message = "Name is required")
    @Length(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @Min(value = 5, message = "Age must be at least 5")
    @Max(value = 100, message = "Age must be at most 100")
    private Integer age;

    @Length(max = 50, message = "Grade must be at most 50 characters")
    private String grade;

    @Email(message = "Email must be valid")
    @Length(max = 100, message = "Email must be at most 100 characters")
    private String email;

    @Length(max = 20, message = "Phone must be at most 20 characters")
    private String phone;

    private String notes;

    private PricingDTO pricing;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public StudentDTO() {
    }

    public StudentDTO(String name, Integer age, String grade) {
        this.name = name;
        this.age = age;
        this.grade = grade;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public PricingDTO getPricing() {
        return pricing;
    }

    public void setPricing(PricingDTO pricing) {
        this.pricing = pricing;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Nested DTO for Pricing
    public static class PricingDTO {
        private Long id;
        
        @NotNull(message = "Hourly rate is required")
        @DecimalMin(value = "0.01", message = "Hourly rate must be positive")
        private BigDecimal hourlyRate;

        @NotNull(message = "Lesson duration is required")
        @Positive(message = "Lesson duration must be positive")
        private Integer lessonDuration;

        private String paymentTerms;

        // Constructors
        public PricingDTO() {
        }

        public PricingDTO(BigDecimal hourlyRate, Integer lessonDuration) {
            this.hourlyRate = hourlyRate;
            this.lessonDuration = lessonDuration;
        }

        // Getters and Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

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
    }

    @Override
    public String toString() {
        return "StudentDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", grade='" + grade + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", hasPricing=" + (pricing != null) +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
