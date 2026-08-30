package org.safa.maintenanceservice.admin.exceptions;

/**
 * This is when connecting to other APIs and still not getting any response
 */
public class NoResponseException extends RuntimeException {
    public NoResponseException(String message) {
        super(message);
    }
}
