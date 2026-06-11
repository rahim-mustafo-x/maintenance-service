package org.safa.maintenanceservice.models.exceptions;

/**
 * It is considered to be used in a moments of creating for instance when a user creates something, but it was already there**/
public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String message) {
        super(message);
    }
}