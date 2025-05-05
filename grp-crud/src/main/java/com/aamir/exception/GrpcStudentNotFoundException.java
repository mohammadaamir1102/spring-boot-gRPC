package com.aamir.exception;

public class GrpcStudentNotFoundException extends RuntimeException {
    public GrpcStudentNotFoundException(String message) {
        super(message);
    }
}