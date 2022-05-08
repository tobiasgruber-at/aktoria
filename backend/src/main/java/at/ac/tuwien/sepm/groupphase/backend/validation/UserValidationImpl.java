package at.ac.tuwien.sepm.groupphase.backend.validation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PasswordChangeDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UpdateUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validation for user.
 *
 * @author Luke Nemeskeri
 */
public class UserValidationImpl implements UserValidation {

    UserRepository userRepository;

    public UserValidationImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void validateCreateUserInput(UserRegistrationDto userRegistrationDto) throws ValidationException, ConflictException {
        try {
            validateNames(userRegistrationDto.getFirstName(), userRegistrationDto.getLastName());
            validateEmail(userRegistrationDto.getEmail(), false);
            validatePassword(userRegistrationDto.getPassword());
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage(), e);
        }
        try {
            checkForEmailConflict(userRegistrationDto.getEmail());
        } catch (ConflictException e) {
            throw new ConflictException(e.getMessage(), e);
        }
    }

    @Override
    public void validatePatchUser(UpdateUserDto updateUserDto) throws ValidationException, ConflictException {
        try {
            validateNamesForPatch(updateUserDto.getFirstName(), updateUserDto.getLastName());
            validateEmail(updateUserDto.getEmail(), true);
            checkForEmailConflict(updateUserDto.getEmail());
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage(), e);
        } catch (ConflictException e) {
            throw new ConflictException(e.getMessage(), e);
        }
    }

    @Override
    public void validateEmailForForgottenPassword(String email) throws NotFoundException {
        if ((userRepository.findByEmail(email)).isEmpty()) {
            throw new NotFoundException("Email does not exist!");
        }
    }

    @Override
    public void validateChangePassword(PasswordChangeDto passwordChangeDto) throws ValidationException {
        try {
            validatePassword(passwordChangeDto.getNewPassword());
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage(), e);
        }
    }

    private void validatePassword(String password) throws ValidationException {
        if (password != null) {
            if (password.trim().length() <= 0) {
                throw new ValidationException("Password must not contain only whitespaces!");
            }
            if (password.length() < 8) {
                throw new ValidationException("Password must be at least 8 characters long!");
            }
        } else {
            throw new ValidationException("Please fill out required fields!");
        }
    }

    private void validateEmail(String email, Boolean canBeNull) throws ValidationException {
        if (email != null) {
            if (email.trim().length() <= 0) {
                throw new ValidationException("Email must not contain only whitespaces!");
            }
            if (email.length() > 100) {
                throw new ValidationException("Email is too long!");
            }
            if (!email.trim().equals(email)) {
                throw new ValidationException("Email must not start or end with whitespaces!");
            }
            Pattern pattern = Pattern.compile(
                "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\"
                    + "x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4]"
                    + "[0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
            Matcher matcher = pattern.matcher(email);
            if (!matcher.matches()) {
                throw new ValidationException("Email is not valid!");
            }
        } else if (!canBeNull) {
            throw new ValidationException("Please fill out required fields!");
        }
    }

    private void validateNamesForPatch(String firstName, String lastName) throws ValidationException {
        if (firstName != null) {
            if (!firstName.trim().equals(firstName)) {
                throw new ValidationException("First-Name must not start or end with whitespaces!");
            }
            if (firstName.trim().length() <= 0) {
                throw new ValidationException("First-Name must not contain only whitespaces!");
            }
            if (firstName.length() > 100) {
                throw new ValidationException("First-Name is too long!");
            }
        }
        if (lastName != null) {
            if (lastName.trim().length() <= 0) {
                throw new ValidationException("Last-Name must not contain only whitespaces!");
            }
            if (lastName.length() > 100) {
                throw new ValidationException("Last-Name is too long!");
            }
            if (!lastName.trim().equals(lastName)) {
                throw new ValidationException("Last-Name must not start or end with whitespaces!");
            }
        }
    }

    private void validateNames(String firstName, String lastName) throws ValidationException {
        if (firstName != null) {
            if (!firstName.trim().equals(firstName)) {
                throw new ValidationException("First-Name must not start or end with whitespaces!");
            }
            if (firstName.trim().length() <= 0) {
                throw new ValidationException("First-Name must not contain only whitespaces!");
            }
            if (firstName.length() > 100) {
                throw new ValidationException("First-Name is too long!");
            }
        } else {
            throw new ValidationException("Please fill out required fields!");
        }
        if (lastName != null) {
            if (lastName.trim().length() <= 0) {
                throw new ValidationException("Last-Name must not contain only whitespaces!");
            }
            if (lastName.length() > 100) {
                throw new ValidationException("Last-Name is too long!");
            }
            if (!lastName.trim().equals(lastName)) {
                throw new ValidationException("Last-Name must not start or end with whitespaces!");
            }
        } else {
            throw new ValidationException("Please fill out required fields!");
        }
    }


    private void checkForEmailConflict(String email) throws ConflictException {
        if ((userRepository.findByEmail(email)).isPresent()) {
            throw new ConflictException("Email is already used!");
        }
    }
}
