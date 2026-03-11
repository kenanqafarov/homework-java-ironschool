package com.example.demo.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.UUID;

public class Teacher {
    private String teacherId;
    @NotBlank(message = "Teacher name cannot be blank")
    private String name;
    @PositiveOrZero(message = "Salary cannot be negative")
    private double salary;

    public Teacher(String name, double salary) {
        this.teacherId = UUID.randomUUID().toString();
        this.name = name;
        this.salary = salary;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}