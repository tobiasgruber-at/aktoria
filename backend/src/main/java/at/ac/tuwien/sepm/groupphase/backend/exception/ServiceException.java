package at.ac.tuwien.sepm.groupphase.backend.exception;

public class ServiceException extends RuntimeException {

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
