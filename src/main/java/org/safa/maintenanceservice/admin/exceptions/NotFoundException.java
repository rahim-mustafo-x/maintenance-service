package org.safa.maintenanceservice.admin.exceptions;


/**
 * It is considered to be used in a moments of login when user is not available or in a moments where product is unavailable**/
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
