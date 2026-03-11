package com.example.demo.Exceptions;

/** Thrown when a student with the specified ID cannot be found. */
public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(String message) {
        super(message);
    }
}