package com.example.demo.Exceptions;

/** Thrown when a caller provides invalid or missing input (e.g., blank name, negative price). */
public class InvalidInputException extends RuntimeException {
    public InvalidInputException(String message) {
        super(message);
    }
}