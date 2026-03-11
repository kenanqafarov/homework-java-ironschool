package com.example.demo.services;

import com.example.demo.Exceptions.CourseNotFoundException;
import com.example.demo.Exceptions.InvalidInputException;
import com.example.demo.models.Course;
import com.example.demo.models.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CourseService {

    private final Map<String, Course> courseMap = new HashMap<>();
    private final TeacherService teacherService;

    @Autowired
    public CourseService(@Lazy TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    public Course createCourse(String name, double price) {
        if (name == null || name.isBlank()) {
            throw new InvalidInputException("Course name cannot be empty");
        }
        if (price <= 0) {
            throw new InvalidInputException("Price must be positive");
        }
        String courseId = UUID.randomUUID().toString();
        Course course = new Course(name, price);
        course.setCourseId(courseId);
        courseMap.put(courseId, course);
        return course;
    }

    public Course getCourseById(String courseId) {
        if (courseId == null) {
            throw new InvalidInputException("Course ID cannot be null");
        }
        Course course = courseMap.get(courseId);
        if (course == null) {
            throw new CourseNotFoundException("Course not found with ID: " + courseId);
        }
        return course;
    }

    public List<Course> getAllCourses() {
        return new ArrayList<>(courseMap.values());
    }

    public void updateCourse(String courseId, String newName, Double newPrice) {
        Course course = getCourseById(courseId);
        if (newName != null && !newName.isBlank()) {
            course.setName(newName);
        }
        if (newPrice != null && newPrice > 0) {
            course.setPrice(newPrice);
        }
    }

    public void deleteCourse(String courseId) {
        getCourseById(courseId);
        courseMap.remove(courseId);
    }

    public void assignTeacherToCourse(String courseId, String teacherId) {
        Course course = getCourseById(courseId);
        Teacher teacher = teacherService.getTeacherById(teacherId);
        course.setTeacher(teacher);
    }

    public List<Course> getCoursesByTeacher(String teacherId) {
        teacherService.getTeacherById(teacherId);
        List<Course> teacherCourses = new ArrayList<>();
        for (Course course : courseMap.values()) {
            if (course.getTeacher() != null && course.getTeacher().getTeacherId().equals(teacherId)) {
                teacherCourses.add(course);
            }
        }
        return teacherCourses;
    }

    public double getCourseProfit(String courseId) {
        Course course = getCourseById(courseId);
        double earned = course.getMoneyEarned();
        double salary = course.getTeacher() != null ? course.getTeacher().getSalary() : 0;
        return earned - salary;
    }

    public double getTotalProfit() {
        double totalEarned = courseMap.values().stream().mapToDouble(Course::getMoneyEarned).sum();
        double totalSalaries = teacherService.getAllTeachers().stream().mapToDouble(Teacher::getSalary).sum();
        return totalEarned - totalSalaries;
    }
}