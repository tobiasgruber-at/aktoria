package at.ac.tuwien.sepm.groupphase.backend.validation.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PasswordChangeDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UpdateUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.validation.UserValidation;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validation for user.
 *
 * @author Luke Nemeskeri
 * @author Simon Josef Kreuzpointner
 */
@Component
public class UserValidationImpl implements UserValidation {
    UserRepository userRepository;

    public UserValidationImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void validateCreateUserInput(UserRegistrationDto userRegistrationDto) throws ValidationException, ConflictException {
        try {
            validateNotNull(userRegistrationDto.getFirstName());
            validateNotNull(userRegistrationDto.getLastName());
            validateNames(userRegistrationDto.getFirstName(), userRegistrationDto.getLastName());
            validateNotNull(userRegistrationDto.getEmail());
            validateEmail(userRegistrationDto.getEmail());
            validatePassword(userRegistrationDto.getPassword());
            checkForEmailConflict(userRegistrationDto.getEmail());
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage(), e);
        } catch (ConflictException e) {
            throw new ConflictException(e.getMessage(), e);
        }
    }

    @Override
    public void validatePatchUserInput(UpdateUserDto updateUserDto) throws ValidationException, ConflictException {
        try {
            validateNames(updateUserDto.getFirstName(), updateUserDto.getLastName());
            validateEmail(updateUserDto.getEmail());
            checkForEmailConflict(updateUserDto.getEmail());
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage(), e);
        } catch (ConflictException e) {
            throw new ConflictException(e.getMessage(), e);
        }
    }

    @Override
    public void validateEmailForForgottenPasswordInput(String email) throws NotFoundException {
        if ((userRepository.findByEmail(email)).isEmpty()) {
            throw new NotFoundException("Email existiert nicht!");
        }
    }

    @Override
    public void validateChangePasswordInput(PasswordChangeDto passwordChangeDto) throws ValidationException {
        try {
            validatePassword(passwordChangeDto.getNewPassword());
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage(), e);
        }
    }

    private void validatePassword(String password) throws ValidationException {
        try {
            validateNotNull(password);
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage(), e);
        }
        if (password.trim().length() <= 0) {
            throw new ValidationException("Passwort darf nicht nur aus Leerzeichen bestehen!");
        }
        if (password.length() < 8) {
            throw new ValidationException("Passwort muss mindestens 8 Zeichen lang sein!");
        }
    }

    private void validateEmail(String email) throws ValidationException {
        if (email != null) {
            if (email.trim().length() <= 0) {
                throw new ValidationException("Email darf nicht nur aus Leerzeichen bestehen!");
            }
            if (email.length() > 100) {
                throw new ValidationException("Email ist zu lange!");
            }
            if (!email.trim().equals(email)) {
                throw new ValidationException("Email darf nicht mit Leerzeichen beginnen oder aufhören!");
            }

            Pattern pattern = Pattern.compile(
                "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\"
                    + "x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4]"
                    + "[0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
            Matcher matcher = pattern.matcher(email);
            if (!matcher.matches()) {
                throw new ValidationException("Email entspricht keinem gültigen Format!");
            }
        }
    }

    private void validateNames(String firstName, String lastName) throws ValidationException {
        if (firstName != null) {
            if (!firstName.trim().equals(firstName)) {
                throw new ValidationException("Vorname darf nicht mit Leerzeichen beginnen oder aufhören!");
            }
            if (firstName.trim().length() <= 0) {
                throw new ValidationException("Vorname darf nicht nur aus Leerzeichen bestehen!");
            }
            if (firstName.length() > 100) {
                throw new ValidationException("Vorname zu lange!");
            }

            Pattern pattern = Pattern.compile("^[\\w\\d-_ \\.]*$");
            Matcher matcher = pattern.matcher(firstName);
            if (!matcher.matches()) {
                throw new ValidationException("Vorname enthält nicht unterstützte Zeichen.");
            }
        }
        if (lastName != null) {
            if (lastName.trim().length() <= 0) {
                throw new ValidationException("Nachname darf nicht nur aus Leerzeichen bestehen!");
            }
            if (lastName.length() > 100) {
                throw new ValidationException("Nachname zu lange!");
            }
            if (!lastName.trim().equals(lastName)) {
                throw new ValidationException("Nachname darf nicht mit Leerzeichen beginnen oder aufhören!");
            }
            Pattern pattern = Pattern.compile("^[\\w\\d-_ \\.]*$");
            Matcher matcher = pattern.matcher(lastName);
            if (!matcher.matches()) {
                throw new ValidationException("Nachname enthält nicht unterstützte Zeichen.");
            }
        }
    }

    private void checkForEmailConflict(String email) throws ConflictException {
        if ((userRepository.findByEmail(email)).isPresent()) {
            throw new ConflictException("Email wird bereits verwendet!");
        }
    }

    private void validateNotNull(Object o) throws ValidationException {
        if (o == null) {
            throw new ValidationException("Nicht alle erforderlichen Felder ausgefüllt!");
        }
    }
}
