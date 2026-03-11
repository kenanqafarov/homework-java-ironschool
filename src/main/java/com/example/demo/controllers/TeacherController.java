package com.example.demo.controllers;

import com.example.demo.models.Teacher;
import com.example.demo.services.TeacherService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @PostMapping
    public ResponseEntity<Teacher> createTeacher(@Valid @RequestBody Teacher teacher) {
        Teacher created = teacherService.createTeacher(teacher.getName(), teacher.getSalary());
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{teacherId}")
    public ResponseEntity<Teacher> getTeacherById(@PathVariable String teacherId) {
        return ResponseEntity.ok(teacherService.getTeacherById(teacherId));
    }

    @GetMapping
    public ResponseEntity<List<Teacher>> getAllTeachers() {
        return ResponseEntity.ok(teacherService.getAllTeachers());
    }

    @PutMapping("/{teacherId}")
    public ResponseEntity<Void> updateTeacher(@PathVariable String teacherId, @RequestBody Map<String, Object> updates) {
        String newName = (String) updates.get("name");
        Double newSalary = updates.containsKey("salary") ? ((Number) updates.get("salary")).doubleValue() : null;
        teacherService.updateTeacher(teacherId, newName, newSalary);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{teacherId}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable String teacherId) {
        teacherService.deleteTeacher(teacherId);
        return ResponseEntity.ok().build();
    }
}