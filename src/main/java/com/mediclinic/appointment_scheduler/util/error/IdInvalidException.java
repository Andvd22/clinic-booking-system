package com.mediclinic.appointment_scheduler.util.error;

public class IdInvalidException extends RuntimeException {
    // constructor nên dùng extends runtimeexception
    public IdInvalidException(String message) {
        super(message);
    }
}
