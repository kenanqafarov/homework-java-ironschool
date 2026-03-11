package com.example.demo.services;

import com.example.demo.Exceptions.StudentAlreadyEnrolledException;
import com.example.demo.models.Course;
import com.example.demo.models.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentServiceTest {

    private StudentService studentService;
    private CourseService courseService;

    @BeforeEach
    void setUp() {
        TeacherService teacherService = new TeacherService(null);
        courseService = new CourseService(teacherService);
        
        studentService = new StudentService(courseService);
    }

    @Test
    void createStudentValid() {
        Student student = studentService.createStudent("Alice", "123 St", "alice@email.com");
        assertNotNull(student.getStudentId());
        assertEquals("Alice", student.getName());
    }

    @Test
    void enrollStudentInCourse() {
        Course course = courseService.createCourse("Math", 100.0);
        Student student = studentService.createStudent("Alice", "123 St", "alice@email.com");
        studentService.enrollStudentInCourse(student.getStudentId(), course.getCourseId());
        assertEquals(course, student.getCourse());
        assertEquals(100.0, course.getMoneyEarned());
    }

    @Test
    void enrollAlreadyEnrolledSameCourse() {
        Course course = courseService.createCourse("Math", 100.0);
        Student student = studentService.createStudent("Alice", "123 St", "alice@email.com");
        studentService.enrollStudentInCourse(student.getStudentId(), course.getCourseId());
        assertThrows(StudentAlreadyEnrolledException.class,
                () -> studentService.enrollStudentInCourse(student.getStudentId(), course.getCourseId()));
    }

    @Test
    void unenrollStudentFromCourse() {
        Course course = courseService.createCourse("Math", 100.0);
        Student student = studentService.createStudent("Alice", "123 St", "alice@email.com");
        studentService.enrollStudentInCourse(student.getStudentId(), course.getCourseId());
        studentService.unenrollStudentFromCourse(student.getStudentId());
        assertNull(student.getCourse());
        assertEquals(0.0, course.getMoneyEarned());
    }
}