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

/**
 * Service layer for managing Course entities.
 * Handles creation, retrieval, updating, deletion, teacher assignment, and profit calculation.
 */
@Service
public class CourseService {

    private final Map<String, Course> courseMap = new HashMap<>();
    private final TeacherService teacherService;

    @Autowired
    public CourseService(@Lazy TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    /**
     * Creates a new course with the given name and price.
     * The course ID is auto-generated inside the Course constructor.
     */
    public Course createCourse(String name, double price) {
        if (name == null || name.isBlank()) {
            throw new InvalidInputException("Course name cannot be empty");
        }
        if (price <= 0) {
            throw new InvalidInputException("Price must be positive");
        }
        Course course = new Course(name, price); // ID auto-generated in constructor
        courseMap.put(course.getCourseId(), course);
        return course;
    }

    /**
     * Retrieves a course by its ID.
     * Throws CourseNotFoundException if no course matches the given ID.
     */
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

    /**
     * Returns all courses in the system.
     */
    public List<Course> getAllCourses() {
        return new ArrayList<>(courseMap.values());
    }

    /**
     * Updates the name and/or price of an existing course.
     * Only non-null and valid values are applied.
     */
    public void updateCourse(String courseId, String newName, Double newPrice) {
        Course course = getCourseById(courseId);
        if (newName != null && !newName.isBlank()) {
            course.setName(newName);
        }
        if (newPrice != null && newPrice > 0) {
            course.setPrice(newPrice);
        }
    }

    /**
     * Deletes a course by its ID.
     * Throws CourseNotFoundException if the course does not exist.
     */
    public void deleteCourse(String courseId) {
        getCourseById(courseId); // Validates existence
        courseMap.remove(courseId);
    }

    /**
     * Assigns a teacher to a course.
     * Both the course and teacher must already exist.
     */
    public void assignTeacherToCourse(String courseId, String teacherId) {
        Course course = getCourseById(courseId);
        Teacher teacher = teacherService.getTeacherById(teacherId);
        course.setTeacher(teacher);
    }

    /**
     * Returns all courses assigned to a specific teacher.
     */
    public List<Course> getCoursesByTeacher(String teacherId) {
        teacherService.getTeacherById(teacherId); // Validates teacher existence
        List<Course> teacherCourses = new ArrayList<>();
        for (Course course : courseMap.values()) {
            if (course.getTeacher() != null && course.getTeacher().getTeacherId().equals(teacherId)) {
                teacherCourses.add(course);
            }
        }
        return teacherCourses;
    }

    /**
     * Calculates profit for a specific course.
     * Profit = moneyEarned - (teacher salary / number of courses the teacher teaches).
     * Teacher salary is distributed evenly across all courses they teach.
     * If no teacher is assigned, profit equals total money earned.
     */
    public double getCourseProfit(String courseId) {
        Course course = getCourseById(courseId);
        double earned = course.getMoneyEarned();

        if (course.getTeacher() == null) {
            return earned;
        }

        String teacherId = course.getTeacher().getTeacherId();
        double teacherSalary = course.getTeacher().getSalary();
        long teacherCourseCount = courseMap.values().stream()
                .filter(c -> c.getTeacher() != null && c.getTeacher().getTeacherId().equals(teacherId))
                .count();

        // Distribute salary evenly across all courses this teacher teaches
        double salaryShare = teacherCourseCount > 0 ? teacherSalary / teacherCourseCount : 0;
        return earned - salaryShare;
    }

    /**
     * Calculates total profit across all courses.
     * Total Profit = total money earned from all courses - total salaries of all teachers.
     */
    public double getTotalProfit() {
        double totalEarned = courseMap.values().stream().mapToDouble(Course::getMoneyEarned).sum();
        double totalSalaries = teacherService.getAllTeachers().stream().mapToDouble(Teacher::getSalary).sum();
        return totalEarned - totalSalaries;
    }
}