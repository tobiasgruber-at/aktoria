package at.ac.tuwien.sepm.groupphase.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for unprocessable email-addresses.
 *
 * @author Nikolaus Peter
 */

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class UnprocessableEmailException extends RuntimeException {

    public UnprocessableEmailException() {
        super();
    }

    public UnprocessableEmailException(String message) {
        super(message);
    }

    public UnprocessableEmailException(Throwable cause) {
        super(cause);
    }

    public UnprocessableEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
