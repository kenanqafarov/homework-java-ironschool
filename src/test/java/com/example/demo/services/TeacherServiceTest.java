package com.example.demo.services;

import com.example.demo.Exceptions.InvalidInputException;
import com.example.demo.models.Course;
import com.example.demo.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeacherServiceTest {

    private TeacherService teacherService;
    private CourseService courseService;

    @BeforeEach
    void setUp() {
        courseService = new CourseService(null); 
        teacherService = new TeacherService(courseService);
    }

    @Test
    void createTeacherValid() {
        Teacher teacher = teacherService.createTeacher("John", 500.0);
        assertNotNull(teacher.getTeacherId());
        assertEquals("John", teacher.getName());
        assertEquals(500.0, teacher.getSalary());
    }

    @Test
    void createTeacherInvalidSalary() {
        assertThrows(InvalidInputException.class, () -> teacherService.createTeacher("John", -100.0));
    }

    @Test
    void deleteTeacherUnassignsCourses() {
        Teacher teacher = teacherService.createTeacher("John", 500.0);
        
        CourseService realCourseService = new CourseService(teacherService);
        TeacherService realTeacherService = new TeacherService(realCourseService);
        
        Teacher t = realTeacherService.createTeacher("John", 500.0);
        Course course = realCourseService.createCourse("Math", 100.0);
        
        realCourseService.assignTeacherToCourse(course.getCourseId(), t.getTeacherId());
        realTeacherService.deleteTeacher(t.getTeacherId());
        
        assertNull(course.getTeacher());
    }

    @Test
    void updateTeacher() {
        Teacher teacher = teacherService.createTeacher("John", 500.0);
        teacherService.updateTeacher(teacher.getTeacherId(), "Jane", 600.0);
        assertEquals("Jane", teacher.getName());
        assertEquals(600.0, teacher.getSalary());
    }
}