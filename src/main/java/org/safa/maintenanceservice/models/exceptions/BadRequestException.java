package org.safa.maintenanceservice.models.exceptions;

/** When data is valid, but logically not for instance start date > end date **/
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}