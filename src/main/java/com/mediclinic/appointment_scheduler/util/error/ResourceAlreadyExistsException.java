package com.mediclinic.appointment_scheduler.util.error;

public class ResourceAlreadyExistsException extends RuntimeException {
    
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}
