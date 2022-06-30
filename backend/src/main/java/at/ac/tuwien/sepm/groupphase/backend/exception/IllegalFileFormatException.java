package at.ac.tuwien.sepm.groupphase.backend.exception;

/**
 * An extension to RuntimeException.
 *
 * @author Simon Josef Kreuzpointner
 */
public class IllegalFileFormatException extends RuntimeException {

    public IllegalFileFormatException(String message) {
        super(message);
    }

}
