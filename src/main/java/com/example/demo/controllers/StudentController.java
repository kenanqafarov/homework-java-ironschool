package com.example.demo.controllers;

import com.example.demo.models.Course;
import com.example.demo.models.Student;
import com.example.demo.services.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@Valid @RequestBody Student student) {
        Student created = studentService.createStudent(student.getName(), student.getAddress(), student.getEmail());
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<Student> getStudentById(@PathVariable String studentId) {
        return ResponseEntity.ok(studentService.getStudentById(studentId));
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<Void> updateStudent(@PathVariable String studentId, @RequestBody Map<String, String> updates) {
        String newName = updates.get("name");
        String newAddress = updates.get("address");
        String newEmail = updates.get("email");
        studentService.updateStudent(studentId, newName, newAddress, newEmail);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> deleteStudent(@PathVariable String studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{studentId}/enroll/{courseId}")
    public ResponseEntity<Void> enrollStudentInCourse(@PathVariable String studentId, @PathVariable String courseId) {
        studentService.enrollStudentInCourse(studentId, courseId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{studentId}/unenroll")
    public ResponseEntity<Void> unenrollStudentFromCourse(@PathVariable String studentId) {
        studentService.unenrollStudentFromCourse(studentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Student>> getEnrolledStudents(@PathVariable String courseId) {
        return ResponseEntity.ok(studentService.getEnrolledStudents(courseId));
    }

    @GetMapping("/{studentId}/course")
    public ResponseEntity<Course> getCourseForStudent(@PathVariable String studentId) {
        return ResponseEntity.ok(studentService.getCourseForStudent(studentId));
    }
}