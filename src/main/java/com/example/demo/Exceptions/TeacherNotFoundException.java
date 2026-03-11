package com.example.demo.Exceptions;

/** Thrown when a teacher with the specified ID cannot be found. */
public class TeacherNotFoundException extends RuntimeException {
    public TeacherNotFoundException(String message) {
        super(message);
    }
}