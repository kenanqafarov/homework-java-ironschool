package com.example.demo.Exceptions;
// dsgsd
public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(String message) {
        super(message);
    }
}
