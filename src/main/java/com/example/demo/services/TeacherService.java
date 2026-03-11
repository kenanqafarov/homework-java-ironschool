package com.example.demo.services;

import com.example.demo.Exceptions.InvalidInputException;
import com.example.demo.Exceptions.TeacherNotFoundException;
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
 * Service layer for managing Teacher entities.
 * Handles creation, retrieval, updating, and deletion of teachers.
 * On deletion, unassigns the teacher from all their courses.
 */
@Service
public class TeacherService {

    private final Map<String, Teacher> teacherMap = new HashMap<>();
    private final CourseService courseService;

    @Autowired
    public TeacherService(@Lazy CourseService courseService) {
        this.courseService = courseService;
    }

    /**
     * Creates a new teacher with the given name and salary.
     * The teacher ID is auto-generated inside the Teacher constructor.
     */
    public Teacher createTeacher(String name, double salary) {
        if (name == null || name.isBlank()) {
            throw new InvalidInputException("Teacher name cannot be empty");
        }
        if (salary < 0) {
            throw new InvalidInputException("Salary cannot be negative");
        }
        Teacher teacher = new Teacher(name, salary); // ID auto-generated in constructor
        teacherMap.put(teacher.getTeacherId(), teacher);
        return teacher;
    }

    /**
     * Retrieves a teacher by their ID.
     * Throws TeacherNotFoundException if no teacher matches the given ID.
     */
    public Teacher getTeacherById(String teacherId) {
        if (teacherId == null) {
            throw new InvalidInputException("Teacher ID cannot be null");
        }
        Teacher teacher = teacherMap.get(teacherId);
        if (teacher == null) {
            throw new TeacherNotFoundException("Teacher not found with ID: " + teacherId);
        }
        return teacher;
    }

    /**
     * Returns all teachers in the system.
     */
    public List<Teacher> getAllTeachers() {
        return new ArrayList<>(teacherMap.values());
    }

    /**
     * Updates one or more fields of an existing teacher.
     * Only non-null and valid values are applied.
     */
    public void updateTeacher(String teacherId, String newName, Double newSalary) {
        Teacher teacher = getTeacherById(teacherId);
        if (newName != null && !newName.isBlank()) {
            teacher.setName(newName);
        }
        if (newSalary != null && newSalary >= 0) {
            teacher.setSalary(newSalary);
        }
    }

    /**
     * Deletes a teacher by their ID.
     * Before removal, unassigns the teacher from all courses they are teaching
     * to maintain data integrity.
     */
    public void deleteTeacher(String teacherId) {
        getTeacherById(teacherId); // Validates existence
        List<Course> courses = courseService.getCoursesByTeacher(teacherId);
        for (Course course : courses) {
            course.setTeacher(null); // Unassign teacher from all their courses
        }
        teacherMap.remove(teacherId);
    }
}