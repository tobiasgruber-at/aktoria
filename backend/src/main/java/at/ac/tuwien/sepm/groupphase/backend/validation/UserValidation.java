package at.ac.tuwien.sepm.groupphase.backend.validation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;

public interface UserValidation {

    void validateCreateUserInput(UserRegistrationDto userRegistrationDto) throws ValidationException, ConflictException;

    void validateChangeUserInput();

}


