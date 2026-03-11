package com.example.demo.services;

import com.example.demo.Exceptions.InvalidInputException;
import com.example.demo.models.Course;
import com.example.demo.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourseServiceTest {

    private CourseService courseService;
    private TeacherService teacherService;

    @BeforeEach
    void setUp() {
        teacherService = new TeacherService(null); 
        courseService = new CourseService(teacherService);
        
    }

    @Test
    void createCourseValid() {
        Course course = courseService.createCourse("Math", 100.0);
        assertNotNull(course.getCourseId());
        assertEquals("Math", course.getName());
        assertEquals(100.0, course.getPrice());
    }

    @Test
    void createCourseInvalidName() {
        assertThrows(InvalidInputException.class, () -> courseService.createCourse("", 100.0));
    }

    @Test
    void getCourseById() {
        Course course = courseService.createCourse("Math", 100.0);
        Course found = courseService.getCourseById(course.getCourseId());
        assertEquals(course, found);
    }

    @Test
    void assignTeacherToCourse() {
        Teacher teacher = teacherService.createTeacher("John", 500.0);
        Course course = courseService.createCourse("Math", 100.0);
        courseService.assignTeacherToCourse(course.getCourseId(), teacher.getTeacherId());
        assertEquals(teacher, course.getTeacher());
    }

    @Test
    void getCourseProfit() {
        Course course = courseService.createCourse("Math", 100.0);
        course.setMoneyEarned(200.0);
        Teacher teacher = teacherService.createTeacher("John", 50.0);
        course.setTeacher(teacher);
        assertEquals(150.0, courseService.getCourseProfit(course.getCourseId()));
    }
}