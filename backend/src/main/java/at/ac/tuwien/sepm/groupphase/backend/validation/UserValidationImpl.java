package at.ac.tuwien.sepm.groupphase.backend.validation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidationImpl implements UserValidation {

    UserRepository userRepository;

    public UserValidationImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void validateCreateUserInput(UserRegistrationDto userRegistrationDto) throws ValidationException, ConflictException {
        try {
            checkIfCreateInputValid(userRegistrationDto.getFirstName(), userRegistrationDto.getLastName(), userRegistrationDto.getEmail(), userRegistrationDto.getPassword());
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage());
        }
        try {
            checkForCreateInputConflict(userRegistrationDto.getEmail());
        } catch (ConflictException e) {
            throw new ConflictException(e.getMessage());
        }
    }

    @Override
    public void validateChangeUserInput() {

    }

    private void checkIfCreateInputValid(String firstName, String lastName, String email, String password) throws ValidationException {
        if (firstName == null || lastName == null || email == null || password == null) {
            throw new ValidationException("Please fill out required fields!");
        }
        if (!firstName.trim().equals(firstName)) {
            throw new ValidationException("First-Name must not start or end with whitespaces!");
        }
        if (firstName.trim().length() <= 0) {
            throw new ValidationException("First-Name must not contain only whitespaces!");
        }
        if (firstName.length() > 100) {
            throw new ValidationException("First-Name is too long!");
        }
        if (lastName.trim().length() <= 0) {
            throw new ValidationException("Last-Name must not contain only whitespaces!");
        }
        if (lastName.length() > 100) {
            throw new ValidationException("Last-Name is too long!");
        }
        if (!lastName.trim().equals(lastName)) {
            throw new ValidationException("Last-Name must not start or end with whitespaces!");
        }
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
        if (password.trim().length() <= 0) {
            throw new ValidationException("Password must not contain only whitespaces!");
        }
        if (password.length() < 8) {
            throw new ValidationException("Password must be at least 8 characters long!");
        }
    }

    private void checkForCreateInputConflict(String email) throws ConflictException {
        if ((userRepository.findByEmail(email)).isPresent()) {
            throw new ConflictException("Email is already used!");
        }
    }
}
