package at.ac.tuwien.sepm.groupphase.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Token is invalid")
public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException() {
        super();
    }

    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }

}
