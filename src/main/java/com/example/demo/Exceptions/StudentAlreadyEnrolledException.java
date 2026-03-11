package com.example.demo.Exceptions;

/** Thrown when a student attempts to enroll in a course they are already enrolled in. */
public class StudentAlreadyEnrolledException extends RuntimeException {
    public StudentAlreadyEnrolledException(String message) {
        super(message);
    }
}