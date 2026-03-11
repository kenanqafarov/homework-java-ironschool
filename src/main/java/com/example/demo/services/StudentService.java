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
import java.util.UUID;

@Service
public class StudentService {

    private final Map<String, Student> studentMap = new HashMap<>();
    private final CourseService courseService;

    @Autowired
    public StudentService(CourseService courseService) {
        this.courseService = courseService;
    }

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
        String studentId = UUID.randomUUID().toString();
        Student student = new Student(name, address, email);
        student.setStudentId(studentId);
        studentMap.put(studentId, student);
        return student;
    }

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

    public List<Student> getAllStudents() {
        return new ArrayList<>(studentMap.values());
    }

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

    public void deleteStudent(String studentId) {
        Student student = getStudentById(studentId);
        if (student.getCourse() != null) {
            unenrollStudentFromCourse(studentId);
        }
        studentMap.remove(studentId);
    }

    public void enrollStudentInCourse(String studentId, String courseId) {
        Student student = getStudentById(studentId);
        Course newCourse = courseService.getCourseById(courseId);
        
        if (student.getCourse() != null && student.getCourse().getCourseId().equals(courseId)) {
            throw new StudentAlreadyEnrolledException("Student is already enrolled in this course");
        }
        
        if (student.getCourse() != null) {
            student.getCourse().setMoneyEarned(student.getCourse().getMoneyEarned() - student.getCourse().getPrice());
        }
        
        student.setCourse(newCourse);
        newCourse.setMoneyEarned(newCourse.getMoneyEarned() + newCourse.getPrice());
    }

    public void unenrollStudentFromCourse(String studentId) {
        Student student = getStudentById(studentId);
        Course course = student.getCourse();
        if (course == null) {
            throw new InvalidInputException("Student is not enrolled in any course");
        }
        course.setMoneyEarned(course.getMoneyEarned() - course.getPrice());
        student.setCourse(null);
    }

    public List<Student> getEnrolledStudents(String courseId) {
        courseService.getCourseById(courseId);
        List<Student> enrolledStudents = new ArrayList<>();
        for (Student student : studentMap.values()) {
            if (student.getCourse() != null && student.getCourse().getCourseId().equals(courseId)) {
                enrolledStudents.add(student);
            }
        }
        return enrolledStudents;
    }

    public Course getCourseForStudent(String studentId) {
        Student student = getStudentById(studentId);
        return student.getCourse();
    }
}