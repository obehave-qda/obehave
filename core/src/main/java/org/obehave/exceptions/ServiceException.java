package org.obehave.exceptions;

/**
 * @author Markus Möslinger
 */
public class ServiceException extends RuntimeException {
    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
    public ServiceException(Throwable cause) {
        super(cause);
    }
}
