# Data Model: Piano Teacher Management System

**Date**: 2025-01-30  
**Purpose**: Define entities, relationships, and validation rules

## Entity Overview

```mermaid
erDiagram
    Teacher ||--o{ Student : manages
    Student ||--o{ Lesson : attends
    Student ||--o{ Availability : has
    Teacher ||--o{ Availability : has
    Lesson ||--o{ Homework : assigns
    Lesson ||--o{ Performance : evaluates
    Student ||--o{ Pricing : has
    
    Teacher {
        Long id
        String name
        String email
        String phone
        LocalTime defaultStartTime
        LocalTime defaultEndTime
        Set<Availability> availabilities
    }
    
    Student {
        Long id
        String name
        Integer age
        String grade
        String email
        String phone
        String notes
        Pricing pricing
        Set<Availability> availabilities
        Set<Lesson> lessons
    }
    
    Availability {
        Long id
        DayOfWeek dayOfWeek
        LocalTime startTime
        LocalTime endTime
        boolean recurring
        LocalDate specificDate
        Person person
    }
    
    Lesson {
        Long id
        LocalDateTime startTime
        LocalDateTime endTime
        String topics
        String techniques
        String materials
        String notes
        LessonStatus status
        Student student
        Set<Homework> homework
        Performance performance
    }
    
    Homework {
        Long id
        String description
        LocalDate dueDate
        HomeworkStatus status
        String completionNotes
        Lesson lesson
    }
    
    Performance {
        Long id
        Integer rating
        String notes
        String improvements
        String achievements
        LocalDateTime evaluationDate
        Lesson lesson
    }
    
    Pricing {
        Long id
        BigDecimal hourlyRate
        BigDecimal lessonDuration
        String paymentTerms
        Student student
    }
```

## Entity Definitions

### Teacher

**Purpose**: Represents the piano teacher managing the system

**Fields**:
- `id` (Long, PK): Unique identifier
- `name` (String): Teacher's full name, required, max 100 chars
- `email` (String): Contact email, required, valid email format
- `phone` (String): Contact phone, optional, max 20 chars
- `defaultStartTime` (LocalTime): Default work day start time
- `defaultEndTime` (LocalTime): Default work day end time

**Validation Rules**:
- Name cannot be empty
- Email must be valid format
- End time must be after start time

### Student

**Purpose**: Represents piano students being taught

**Fields**:
- `id` (Long, PK): Unique identifier
- `name` (String): Student's full name, required, max 100 chars, supports Chinese
- `age` (Integer): Student's age, required, min 5, max 100
- `grade` (String): School grade or level, optional, max 50 chars
- `email` (String): Contact email, optional, valid format
- `phone` (String): Contact phone, optional, max 20 chars
- `notes` (String): Additional notes, optional, max 1000 chars, supports Chinese

**Validation Rules**:
- Name cannot be empty
- Age must be between 5 and 100
- Email must be valid if provided

### Availability

**Purpose**: Time slots when teacher or student is available

**Fields**:
- `id` (Long, PK): Unique identifier
- `dayOfWeek` (DayOfWeek): Day of week (MONDAY-SUNDAY)
- `startTime` (LocalTime): Start time of availability
- `endTime` (LocalTime): End time of availability
- `recurring` (Boolean): Whether this repeats weekly
- `specificDate` (LocalDate): Specific date if non-recurring
- `person` (Person): Reference to Teacher or Student

**Validation Rules**:
- End time must be after start time
- Either recurring=true or specificDate must be provided
- No overlapping availability for same person

### Lesson

**Purpose**: Scheduled piano lessons with detailed information

**Fields**:
- `id` (Long, PK): Unique identifier
- `startTime` (LocalDateTime): Lesson start time, required
- `endTime` (LocalDateTime): Lesson end time, required
- `topics` (String): Topics covered, optional, max 1000 chars, supports Chinese
- `techniques` (String): Techniques practiced, optional, max 1000 chars, supports Chinese
- `materials` (String): Teaching materials used, optional, max 500 chars
- `notes` (String): General lesson notes, optional, max 2000 chars, supports Chinese
- `status` (LessonStatus): SCHEDULED, COMPLETED, CANCELLED, RESCHEDULED
- `student` (Student): Reference to attending student

**Validation Rules**:
- End time must be after start time
- Cannot overlap with other lessons for same student
- Student must be available during lesson time
- Teacher must be available during lesson time

### Homework

**Purpose**: Assignments given to students

**Fields**:
- `id` (Long, PK): Unique identifier
- `description` (String): Assignment details, required, max 2000 chars, supports Chinese
- `dueDate` (LocalDate): Due date for completion, required
- `status` (HomeworkStatus): ASSIGNED, IN_PROGRESS, COMPLETED, OVERDUE
- `completionNotes` (String): Notes about completion, optional, max 1000 chars
- `lesson` (Lesson): Reference to assigning lesson

**Validation Rules**:
- Description cannot be empty
- Due date must be after lesson date
- Status transitions follow business rules

### Performance

**Purpose**: Student performance evaluations

**Fields**:
- `id` (Long, PK): Unique identifier
- `rating` (Integer): Performance rating 1-10, required
- `notes` (String): Performance notes, optional, max 2000 chars, supports Chinese
- `improvements` (String): Areas for improvement, optional, max 1000 chars
- `achievements` (String): Notable achievements, optional, max 1000 chars
- `evaluationDate` (LocalDateTime): When evaluation was made, required
- `lesson` (Lesson): Reference to evaluated lesson

**Validation Rules**:
- Rating must be between 1 and 10
- Evaluation date cannot be in future
- Must be associated with a completed lesson

### Pricing

**Purpose**: Billing information for students

**Fields**:
- `id` (Long, PK): Unique identifier
- `hourlyRate` (BigDecimal): Rate per hour, required, positive
- `lessonDuration` (BigDecimal): Standard lesson duration in hours, required
- `paymentTerms` (String): Payment terms, optional, max 500 chars
- `student` (Student): Reference to student

**Validation Rules**:
- Hourly rate must be positive
- Lesson duration must be positive
- One pricing record per student

## Enums

### LessonStatus
- `SCHEDULED`: Lesson is planned
- `COMPLETED`: Lesson has finished
- `CANCELLED`: Lesson was cancelled
- `RESCHEDULED`: Lesson was moved to new time

### HomeworkStatus
- `ASSIGNED`: Homework given to student
- `IN_PROGRESS`: Student is working on it
- `COMPLETED`: Homework finished
- `OVERDUE`: Past due date not completed

## Business Rules

### Scheduling Rules
1. No double-booking of teacher time slots
2. No double-booking of student time slots
3. Lessons must be within both teacher and student availability
4. Minimum lesson duration: 30 minutes
5. Maximum lesson duration: 2 hours

### Homework Rules
1. Homework can only be assigned to completed lessons
2. Due date must be at least 1 day after assignment
3. Status automatically updates to OVERDUE after due date passes

### Performance Rules
1. Performance can only be recorded for completed lessons
2. Only one performance record per lesson
3. Rating scale: 1 (poor) to 10 (excellent)

### Data Integrity
1. Cascade delete: When student is deleted, related lessons, homework, and performance records are deleted
2. Soft delete for lessons (mark as cancelled rather than delete)
3. Audit trail: All modifications include timestamp and user

## Indexing Strategy

### Primary Indexes
- All primary keys automatically indexed

### Secondary Indexes
- Student.name (for search)
- Lesson.startTime (for calendar views)
- Lesson.student_id (for student history)
- Homework.dueDate (for overdue tracking)
- Availability.dayOfWeek (for scheduling queries)

## Data Migration

### Initial Data
1. Create default teacher record
2. Set up default availability patterns
3. Initialize system configuration

### Versioning
- Use Flyway for database migrations
- Version all schema changes
- Provide rollback scripts for major changes
