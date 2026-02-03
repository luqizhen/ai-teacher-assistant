package com.pianoteacher.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

import java.util.regex.Pattern;

@Entity
@Table(name = "students", indexes = {
    @Index(name = "idx_student_name", columnList = "name"),
    @Index(name = "idx_student_email", columnList = "email"),
    @Index(name = "idx_student_created_at", columnList = "created_at")
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Student extends BaseEntity {

    @NotBlank(message = "Name is required")
    @Length(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Min(value = 5, message = "Age must be at least 5")
    @Max(value = 100, message = "Age must be at most 100")
    @Column(name = "age", nullable = false)
    private Integer age;

    @Length(max = 50, message = "Grade must be at most 50 characters")
    @Column(name = "grade", length = 50)
    private String grade;

    @Email(message = "Email must be valid")
    @Length(max = 100, message = "Email must be at most 100 characters")
    @Column(name = "email", length = 100)
    private String email;

    @Length(max = 20, message = "Phone must be at most 20 characters")
    @Column(name = "phone", length = 20)
    private String phone;

    @Lob
    @Column(name = "notes")
    private String notes;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "pricing_id")
    private Pricing pricing;

    // Constructors
    public Student() {
    }

    public Student(String name, Integer age, String grade) {
        this.name = name;
        this.age = age;
        this.grade = grade;
    }

    // Getters and Setters
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

    public Pricing getPricing() {
        return pricing;
    }

    public void setPricing(Pricing pricing) {
        this.pricing = pricing;
    }

    // Business methods
    public void validate() {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (age == null || age < 5 || age > 100) {
            throw new IllegalArgumentException("Age must be between 5 and 100");
        }
        if (email != null && !email.trim().isEmpty() && !isValidEmail(email)) {
            throw new IllegalArgumentException("Email must be valid");
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    public boolean hasValidPricing() {
        return pricing != null && pricing.getHourlyRate() != null && pricing.getHourlyRate().doubleValue() > 0;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", grade='" + grade + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", hasPricing=" + (pricing != null) +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        // Standard JPA entity equality: use ID if both have IDs
        if (getId() != null && student.getId() != null) {
            return getId().equals(student.getId());
        }
        
        // If one has ID and other doesn't, they're not equal
        if (getId() != null || student.getId() != null) {
            return false;
        }
        
        // Both have null IDs - use identity comparison (different objects are not equal)
        return false;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (age != null ? age.hashCode() : 0);
        result = 31 * result + (grade != null ? grade.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (notes != null ? notes.hashCode() : 0);
        result = 31 * result + (pricing != null ? pricing.hashCode() : 0);
        return result;
    }
}
