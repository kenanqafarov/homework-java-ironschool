package com.example.demo.services;

import com.example.demo.Exceptions.InvalidInputException;
import com.example.demo.Exceptions.TeacherNotFoundException;
import com.example.demo.models.Teacher;
import com.example.demo.models.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TeacherService {

    private final Map<String, Teacher> teacherMap = new HashMap<>();
    private final CourseService courseService;

    @Autowired
    public TeacherService(@Lazy CourseService courseService) {
        this.courseService = courseService;
    }

    public Teacher createTeacher(String name, double salary) {
        if (name == null || name.isBlank()) {
            throw new InvalidInputException("Teacher name cannot be empty");
        }
        if (salary < 0) {
            throw new InvalidInputException("Salary cannot be negative");
        }
        Teacher teacher = new Teacher(name, salary);
        teacherMap.put(teacher.getTeacherId(), teacher);
        return teacher;
    }

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

    public List<Teacher> getAllTeachers() {
        return new ArrayList<>(teacherMap.values());
    }

    public void updateTeacher(String teacherId, String newName, Double newSalary) {
        Teacher teacher = getTeacherById(teacherId);
        if (newName != null && !newName.isBlank()) {
            teacher.setName(newName);
        }
        if (newSalary != null && newSalary >= 0) {
            teacher.setSalary(newSalary);
        }
    }

    public void deleteTeacher(String teacherId) {
        getTeacherById(teacherId);
        List<Course> courses = courseService.getCoursesByTeacher(teacherId);
        for (Course course : courses) {
            course.setTeacher(null);
        }
        teacherMap.remove(teacherId);
    }
}