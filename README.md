# School Management System

A RESTful Spring Boot application for managing teachers, students, and courses at a school.

---

## Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+

### Run the Application
```bash
mvn spring-boot:run
```
The server starts at `http://localhost:8080`.

---

## API Reference

### Teachers

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/teachers` | Create a teacher |
| GET | `/teachers` | List all teachers |
| GET | `/teachers/{id}` | Get teacher by ID |
| PUT | `/teachers/{id}` | Update teacher |
| DELETE | `/teachers/{id}` | Delete teacher |

**Create a teacher:**
```bash
curl -X POST http://localhost:8080/teachers \
  -H "Content-Type: application/json" \
  -d '{"name": "Alice Smith", "salary": 3000.0}'
```

---

### Courses

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/courses` | Create a course |
| GET | `/courses` | List all courses |
| GET | `/courses/{id}` | Get course by ID |
| PUT | `/courses/{id}` | Update course |
| DELETE | `/courses/{id}` | Delete course |
| POST | `/courses/{courseId}/assign-teacher/{teacherId}` | Assign teacher to course |
| GET | `/courses/teacher/{teacherId}` | Get all courses by a teacher |
| GET | `/courses/profit/{courseId}` | Get profit for a course |
| GET | `/courses/total-profit` | Get total school profit |

**Create a course:**
```bash
curl -X POST http://localhost:8080/courses \
  -H "Content-Type: application/json" \
  -d '{"name": "Mathematics", "price": 500.0}'
```

**Assign a teacher to a course:**
```bash
curl -X POST http://localhost:8080/courses/{courseId}/assign-teacher/{teacherId}
```

---

### Students

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/students` | Create a student |
| GET | `/students/{id}` | Get student by ID |
| PUT | `/students/{id}` | Update student |
| DELETE | `/students/{id}` | Delete student |
| POST | `/students/{studentId}/enroll/{courseId}` | Enroll student in course |
| DELETE | `/students/{studentId}/unenroll` | Unenroll student |
| GET | `/students/course/{courseId}` | Get all students in a course |
| GET | `/students/{studentId}/course` | Get course for a student |

**Create a student:**
```bash
curl -X POST http://localhost:8080/students \
  -H "Content-Type: application/json" \
  -d '{"name": "Bob Jones", "address": "123 Main St", "email": "bob@example.com"}'
```

**Enroll a student in a course:**
```bash
curl -X POST http://localhost:8080/students/{studentId}/enroll/{courseId}
```

---

## Profit Calculation

- **Course Profit**: `moneyEarned - (teacherSalary / numberOfCoursesTeacherTeaches)`
- **Total Profit**: `totalMoneyEarned - totalTeacherSalaries`

---

## Error Handling

All errors return a meaningful HTTP status and message body:

| Situation | HTTP Status |
|-----------|-------------|
| Entity not found | 404 Not Found |
| Student already enrolled | 409 Conflict |
| Invalid input (blank, negative) | 400 Bad Request |
| Validation failure | 400 Bad Request |
| Unexpected error | 500 Internal Server Error |

---

## Project Structure

```
src/main/java/com/example/demo/
├── controllers/
│   ├── CourseController.java
│   ├── StudentController.java
│   └── TeacherController.java
├── models/
│   ├── Course.java
│   ├── Student.java
│   └── Teacher.java
├── services/
│   ├── CourseService.java
│   ├── StudentService.java
│   └── TeacherService.java
└── Exceptions/
    ├── GlobalExceptionHandler.java
    ├── CourseNotFoundException.java
    ├── StudentNotFoundException.java
    ├── TeacherNotFoundException.java
    ├── StudentAlreadyEnrolledException.java
    └── InvalidInputException.java
```