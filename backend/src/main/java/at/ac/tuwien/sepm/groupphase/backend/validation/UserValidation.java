package at.ac.tuwien.sepm.groupphase.backend.validation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PasswordChangeDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UpdateUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import org.springframework.stereotype.Component;

@Component
public interface UserValidation {

    void validateCreateUserInput(UserRegistrationDto userRegistrationDto) throws ValidationException, ConflictException;

    void validatePatchUserInput(UpdateUserDto updateUserDto) throws ValidationException, ConflictException;

    void validateEmailForForgottenPasswordInput(String email) throws NotFoundException;

    void validateEmail(String email);

    void validateChangePasswordInput(PasswordChangeDto passwordChangeDto) throws ValidationException;
}
