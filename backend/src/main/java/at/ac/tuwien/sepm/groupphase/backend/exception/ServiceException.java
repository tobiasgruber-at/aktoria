package at.ac.tuwien.sepm.groupphase.backend.exception;

/**
 * Exception for service exceptions.
 *
 * @author Luke Nemeskeri
 */

public class ServiceException extends RuntimeException {
    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
