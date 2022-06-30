package at.ac.tuwien.sepm.groupphase.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class UnprocessableEmailException extends RuntimeException {

    public UnprocessableEmailException(Throwable cause) {
        super(cause);
    }

}
