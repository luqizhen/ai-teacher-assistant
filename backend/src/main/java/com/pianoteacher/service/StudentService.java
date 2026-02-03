package com.pianoteacher.service;

import com.pianoteacher.model.Student;
import com.pianoteacher.model.Pricing;
import com.pianoteacher.repository.StudentRepository;
import com.pianoteacher.repository.PricingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;
    private final PricingRepository pricingRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository, PricingRepository pricingRepository) {
        this.studentRepository = studentRepository;
        this.pricingRepository = pricingRepository;
    }

    // Student CRUD operations
    public Student createStudent(Student student) {
        student.validate();
        
        // Check for duplicate email
        if (student.getEmail() != null && !student.getEmail().trim().isEmpty()) {
            if (studentRepository.findByEmailIgnoreCase(student.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Student with email " + student.getEmail() + " already exists");
            }
        }
        
        // Check for duplicate phone
        if (student.getPhone() != null && !student.getPhone().trim().isEmpty()) {
            if (studentRepository.findByPhone(student.getPhone()).isPresent()) {
                throw new IllegalArgumentException("Student with phone " + student.getPhone() + " already exists");
            }
        }
        
        // Save pricing if it exists
        if (student.getPricing() != null) {
            student.getPricing().validate();
            Pricing savedPricing = pricingRepository.save(student.getPricing());
            student.setPricing(savedPricing);
        }
        
        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, Student studentDetails) {
        Student existingStudent = getStudentById(id);
        
        // Check for duplicate email (excluding current student)
        if (studentDetails.getEmail() != null && !studentDetails.getEmail().trim().isEmpty()) {
            if (studentRepository.existsByEmailAndIdNot(studentDetails.getEmail(), id)) {
                throw new IllegalArgumentException("Student with email " + studentDetails.getEmail() + " already exists");
            }
        }
        
        // Check for duplicate phone (excluding current student)
        if (studentDetails.getPhone() != null && !studentDetails.getPhone().trim().isEmpty()) {
            if (studentRepository.existsByPhoneAndIdNot(studentDetails.getPhone(), id)) {
                throw new IllegalArgumentException("Student with phone " + studentDetails.getPhone() + " already exists");
            }
        }
        
        // Update student details
        existingStudent.setName(studentDetails.getName());
        existingStudent.setAge(studentDetails.getAge());
        existingStudent.setGrade(studentDetails.getGrade());
        existingStudent.setEmail(studentDetails.getEmail());
        existingStudent.setPhone(studentDetails.getPhone());
        existingStudent.setNotes(studentDetails.getNotes());
        
        // Handle pricing updates
        if (studentDetails.getPricing() != null) {
            studentDetails.getPricing().validate();
            
            if (existingStudent.getPricing() != null) {
                // Update existing pricing
                Pricing existingPricing = existingStudent.getPricing();
                existingPricing.setHourlyRate(studentDetails.getPricing().getHourlyRate());
                existingPricing.setLessonDuration(studentDetails.getPricing().getLessonDuration());
                existingPricing.setPaymentTerms(studentDetails.getPricing().getPaymentTerms());
                pricingRepository.save(existingPricing);
            } else {
                // Add new pricing
                Pricing newPricing = pricingRepository.save(studentDetails.getPricing());
                existingStudent.setPricing(newPricing);
            }
        } else if (existingStudent.getPricing() != null && studentDetails.getPricing() == null) {
            // Remove pricing
            pricingRepository.delete(existingStudent.getPricing());
            existingStudent.setPricing(null);
        }
        
        existingStudent.validate();
        return studentRepository.save(existingStudent);
    }

    public void deleteStudent(Long id) {
        Student student = getStudentById(id);
        
        // Delete associated pricing if exists
        if (student.getPricing() != null) {
            pricingRepository.delete(student.getPricing());
        }
        
        studentRepository.delete(student);
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + id));
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Search and filter operations
    public List<Student> searchStudentsByName(String name) {
        return studentRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Student> searchStudents(String searchTerm) {
        return studentRepository.searchStudents(searchTerm);
    }

    public List<Student> getStudentsByAgeRange(Integer minAge, Integer maxAge) {
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    public List<Student> getStudentsByGrade(String grade) {
        return studentRepository.findByGradeIgnoreCase(grade);
    }

    public List<Student> getStudentsWithPricing() {
        return studentRepository.findStudentsWithPricing();
    }

    public List<Student> getStudentsWithoutPricing() {
        return studentRepository.findStudentsWithoutPricing();
    }

    // Pricing operations
    public Student addPricingToStudent(Long studentId, Pricing pricing) {
        Student student = getStudentById(studentId);
        pricing.validate();
        
        if (student.getPricing() != null) {
            // Update existing pricing
            Pricing existingPricing = student.getPricing();
            existingPricing.setHourlyRate(pricing.getHourlyRate());
            existingPricing.setLessonDuration(pricing.getLessonDuration());
            existingPricing.setPaymentTerms(pricing.getPaymentTerms());
            pricingRepository.save(existingPricing);
        } else {
            // Add new pricing
            Pricing savedPricing = pricingRepository.save(pricing);
            student.setPricing(savedPricing);
        }
        
        return studentRepository.save(student);
    }

    public Student removePricingFromStudent(Long studentId) {
        Student student = getStudentById(studentId);
        
        if (student.getPricing() != null) {
            pricingRepository.delete(student.getPricing());
            student.setPricing(null);
        }
        
        return studentRepository.save(student);
    }

    // Statistics and reporting
    public List<Object[]> getStudentCountByGrade() {
        return studentRepository.countStudentsByGrade();
    }

    public BigDecimal getAverageHourlyRate() {
        return pricingRepository.findAverageHourlyRate();
    }

    public List<Object[]> getMostCommonLessonDurations() {
        return pricingRepository.findMostCommonLessonDurations();
    }

    // Utility methods
    public boolean studentExists(Long id) {
        return studentRepository.existsById(id);
    }

    public Optional<Student> getStudentByEmail(String email) {
        return studentRepository.findByEmailIgnoreCase(email);
    }

    public Optional<Student> getStudentByPhone(String phone) {
        return studentRepository.findByPhone(phone);
    }

    public List<Student> getStudentsCreatedInDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return studentRepository.findStudentsByDateRange(startDate, endDate);
    }
}
