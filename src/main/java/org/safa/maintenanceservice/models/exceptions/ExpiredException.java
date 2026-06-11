package org.safa.maintenanceservice.models.exceptions;

/**When a user tries to do something with data which was already been expired**/
public class ExpiredException extends RuntimeException {
    public ExpiredException(String message) {
        super(message);
    }
}
