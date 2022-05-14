package at.ac.tuwien.sepm.groupphase.backend.exception;

/**
 * An extension to RuntimeException.
 *
 * @author Simon Josef Kreuzpointner
 */
public class IllegalFileFormatException extends Exception {

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
