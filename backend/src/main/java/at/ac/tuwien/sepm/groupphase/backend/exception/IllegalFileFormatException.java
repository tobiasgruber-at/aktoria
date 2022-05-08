package at.ac.tuwien.sepm.groupphase.backend.exception;

public class IllegalFileFormatException extends RuntimeException {

    public IllegalFileFormatException() {
        super();
    }

    public IllegalFileFormatException(String message) {
        super(message);
    }

    public IllegalFileFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalFileFormatException(Exception e) {
        super(e);
    }
}
