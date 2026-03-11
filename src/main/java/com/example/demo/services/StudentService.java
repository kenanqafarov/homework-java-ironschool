package com.example.demo.services;

import com.example.demo.Exceptions.InvalidInputException;
import com.example.demo.Exceptions.StudentAlreadyEnrolledException;
import com.example.demo.Exceptions.StudentNotFoundException;
import com.example.demo.models.Course;
import com.example.demo.models.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service layer for managing Student entities.
 * Handles creation, retrieval, updating, deletion, enrollment, and unenrollment.
 */
@Service
public class StudentService {

    private final Map<String, Student> studentMap = new HashMap<>();
    private final CourseService courseService;

    @Autowired
    public StudentService(CourseService courseService) {
        this.courseService = courseService;
    }

    /**
     * Creates a new student with the given name, address, and email.
     * The student ID is auto-generated inside the Student constructor.
     */
    public Student createStudent(String name, String address, String email) {
        if (name == null || name.isBlank()) {
            throw new InvalidInputException("Student name cannot be empty");
        }
        if (address == null || address.isBlank()) {
            throw new InvalidInputException("Student address cannot be empty");
        }
        if (email == null || email.isBlank()) {
            throw new InvalidInputException("Student email cannot be empty");
        }
        Student student = new Student(name, address, email); // ID auto-generated in constructor
        studentMap.put(student.getStudentId(), student);
        return student;
    }

    /**
     * Retrieves a student by their ID.
     * Throws StudentNotFoundException if no student matches the given ID.
     */
    public Student getStudentById(String studentId) {
        if (studentId == null) {
            throw new InvalidInputException("Student ID cannot be null");
        }
        Student student = studentMap.get(studentId);
        if (student == null) {
            throw new StudentNotFoundException("Student not found with ID: " + studentId);
        }
        return student;
    }

    /**
     * Returns all students in the system.
     */
    public List<Student> getAllStudents() {
        return new ArrayList<>(studentMap.values());
    }

    /**
     * Updates one or more fields of an existing student.
     * Only non-null and non-blank values are applied.
     */
    public void updateStudent(String studentId, String newName, String newAddress, String newEmail) {
        Student student = getStudentById(studentId);
        if (newName != null && !newName.isBlank()) {
            student.setName(newName);
        }
        if (newAddress != null && !newAddress.isBlank()) {
            student.setAddress(newAddress);
        }
        if (newEmail != null && !newEmail.isBlank()) {
            student.setEmail(newEmail);
        }
    }

    /**
     * Deletes a student by their ID.
     * If the student is enrolled in a course, they are unenrolled first to maintain data integrity.
     */
    public void deleteStudent(String studentId) {
        Student student = getStudentById(studentId);
        if (student.getCourse() != null) {
            unenrollStudentFromCourse(studentId); // Clean up enrollment before deletion
        }
        studentMap.remove(studentId);
    }

    /**
     * Enrolls a student in a course.
     * If already enrolled in the same course, throws StudentAlreadyEnrolledException.
     * If enrolled in a different course, automatically unenrolls from the previous course first.
     */
    public void enrollStudentInCourse(String studentId, String courseId) {
        Student student = getStudentById(studentId);
        Course newCourse = courseService.getCourseById(courseId);

        if (student.getCourse() != null && student.getCourse().getCourseId().equals(courseId)) {
            throw new StudentAlreadyEnrolledException("Student is already enrolled in this course");
        }

        // If switching courses, subtract earnings from the old course
        if (student.getCourse() != null) {
            student.getCourse().setMoneyEarned(student.getCourse().getMoneyEarned() - student.getCourse().getPrice());
        }

        student.setCourse(newCourse);
        newCourse.setMoneyEarned(newCourse.getMoneyEarned() + newCourse.getPrice());
    }

    /**
     * Unenrolls a student from their current course.
     * Throws InvalidInputException if the student is not currently enrolled.
     */
    public void unenrollStudentFromCourse(String studentId) {
        Student student = getStudentById(studentId);
        Course course = student.getCourse();
        if (course == null) {
            throw new InvalidInputException("Student is not enrolled in any course");
        }
        course.setMoneyEarned(course.getMoneyEarned() - course.getPrice());
        student.setCourse(null);
    }

    /**
     * Returns all students currently enrolled in the specified course.
     */
    public List<Student> getEnrolledStudents(String courseId) {
        courseService.getCourseById(courseId); // Validates course existence
        List<Student> enrolledStudents = new ArrayList<>();
        for (Student student : studentMap.values()) {
            if (student.getCourse() != null && student.getCourse().getCourseId().equals(courseId)) {
                enrolledStudents.add(student);
            }
        }
        return enrolledStudents;
    }

    /**
     * Returns the course a student is currently enrolled in.
     * Returns null if the student is not enrolled in any course.
     */
    public Course getCourseForStudent(String studentId) {
        Student student = getStudentById(studentId);
        return student.getCourse();
    }
}