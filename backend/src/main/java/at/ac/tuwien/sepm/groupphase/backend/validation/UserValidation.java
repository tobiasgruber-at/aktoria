package at.ac.tuwien.sepm.groupphase.backend.validation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PasswordChangeDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UpdateUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import org.springframework.stereotype.Component;

/**
 * Validation for user.
 *
 * @author Luke Nemeskeri
 */
@Component
public interface UserValidation {

    /**
     * Validate input when creating a new user.
     *
     * @param userRegistrationDto filled with the user input
     * @throws ValidationException is thrown when the data is not valid
     * @throws ConflictException   is thrown when the email is already in use
     */
    void validateCreateUserInput(UserRegistrationDto userRegistrationDto);

    /**
     * validate input when patching a user.
     *
     * @param updateUserDto filled with the new data for the user
     * @throws ValidationException is thrown when the data is not valid
     * @throws ConflictException   is thrown when the email is already in use
     */
    void validatePatchUserInput(UpdateUserDto updateUserDto);

    /**
     * Validates the new password.
     *
     * @param passwordChangeDto filled with the old and new password
     * @param id                the id of the user
     * @throws ValidationException is thrown when the new password is not valid
     * @throws ConflictException   is thrown when the old password does not match the password stored in the data base
     * @throws NotFoundException   is thrown if no user with such id exists
     */
    void validateChangePasswordInput(PasswordChangeDto passwordChangeDto, Long id);

}
