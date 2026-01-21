package com.duyduc.workout_tracker.exception;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
