package at.ac.tuwien.sepm.groupphase.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An extension to RuntimeException.
 *
 * @author Simon Josef Kreuzpointner
 */
@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
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
