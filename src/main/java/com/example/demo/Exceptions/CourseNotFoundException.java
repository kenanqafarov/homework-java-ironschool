package com.example.demo.Exceptions;

/** Thrown when a course with the specified ID cannot be found. */
public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(String message) {
        super(message);
    }
}