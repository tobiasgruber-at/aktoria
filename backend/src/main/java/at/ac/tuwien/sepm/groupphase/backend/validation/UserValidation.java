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
    void validateCreateUserInput(UserRegistrationDto userRegistrationDto) throws ValidationException, ConflictException;

    /**
     * validate input when patching a user.
     *
     * @param updateUserDto filled with the new data for the user
     * @throws ValidationException is thrown when the data is not valid
     * @throws ConflictException   is thrown when the email is already in use
     */
    void validatePatchUser(UpdateUserDto updateUserDto) throws ValidationException, ConflictException;

    /**
     * Validates if email is correct.
     *
     * @param email the email to send the link for the password reset
     * @throws NotFoundException is thrown if the email does not exist in the data base
     */
    void validateEmailForForgottenPassword(String email) throws NotFoundException;

    /**
     * Validates the new password.
     *
     * @param passwordChangeDto filled with the old and new password
     * @throws ValidationException is thrown when the new password is not valid
     */
    void validateChangePassword(PasswordChangeDto passwordChangeDto) throws ValidationException;

}


