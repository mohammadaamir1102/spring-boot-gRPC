package com.aamir.exception;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;

public class GrpcExceptionHandler {

    public static StatusRuntimeException handleException(Exception e) {
        // 1️⃣ Check if the exception is an instance of your custom "not found" exception.
        if (e instanceof GrpcStudentNotFoundException) {
            // ✅ Return a gRPC NOT_FOUND status with the exception's message as the description.
            // This will map to HTTP status 404 in many clients.
            return Status.NOT_FOUND
                    .withDescription(e.getMessage()) // send the specific error message
                    .asRuntimeException(); // convert to a gRPC runtime exception
        }
        // 2️⃣ (Extendable) Add more else-if blocks here to handle other custom exceptions.
        // Example:
        // else if (e instanceof GrpcValidationException) {
        //     return Status.INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException();
        // }

        else {
            // ❌ Default case: For any unhandled exceptions, return INTERNAL error.
            // This is like HTTP 500 – a general server error.
            return Status.INTERNAL
                    .withDescription("Something went wrong") // generic error message
                    .asRuntimeException(); // convert to a gRPC runtime exception
        }
    }

}
