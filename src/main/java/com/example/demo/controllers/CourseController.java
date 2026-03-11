package com.example.demo.controllers;

import com.example.demo.models.Course;
import com.example.demo.services.CourseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @PostMapping
    public ResponseEntity<Course> createCourse(@Valid @RequestBody Course course) {
        Course created = courseService.createCourse(course.getName(), course.getPrice());
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Course> getCourseById(@PathVariable String courseId) {
        return ResponseEntity.ok(courseService.getCourseById(courseId));
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<Void> updateCourse(@PathVariable String courseId, @RequestBody Map<String, Object> updates) {
        String newName = (String) updates.get("name");
        Double newPrice = updates.containsKey("price") ? ((Number) updates.get("price")).doubleValue() : null;
        courseService.updateCourse(courseId, newName, newPrice);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable String courseId) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{courseId}/assign-teacher/{teacherId}")
    public ResponseEntity<Void> assignTeacherToCourse(@PathVariable String courseId, @PathVariable String teacherId) {
        courseService.assignTeacherToCourse(courseId, teacherId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<Course>> getCoursesByTeacher(@PathVariable String teacherId) {
        return ResponseEntity.ok(courseService.getCoursesByTeacher(teacherId));
    }

    @GetMapping("/profit/{courseId}")
    public ResponseEntity<Double> getCourseProfit(@PathVariable String courseId) {
        return ResponseEntity.ok(courseService.getCourseProfit(courseId));
    }

    @GetMapping("/total-profit")
    public ResponseEntity<Double> getTotalProfit() {
        return ResponseEntity.ok(courseService.getTotalProfit());
    }
}