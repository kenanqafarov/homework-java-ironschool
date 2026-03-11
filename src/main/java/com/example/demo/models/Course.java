package com.example.demo.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class Course {
    private String courseId;
    @NotBlank(message = "Course name cannot be blank")
    private String name;
    @Positive(message = "Price must be positive")
    private double price;
    private double moneyEarned;
    private Teacher teacher;

    public Course(String name, double price) {
        this.name = name;
        this.price = price;
        this.moneyEarned = 0.0;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getMoneyEarned() {
        return moneyEarned;
    }

    public void setMoneyEarned(double moneyEarned) {
        this.moneyEarned = moneyEarned;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}