package at.ac.tuwien.sepm.groupphase.backend.validation.impl;

import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.validation.RoleValidation;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RoleValidationImpl implements RoleValidation {
    @Override
    public void validateRoleName(String name) {
        if (name == null) {
            throw new ValidationException("Kein Rollennamen vorhanden");
        }
        if (!(name.equals(name.toUpperCase(Locale.GERMAN)))) {
            throw new ValidationException("Rolle muss in Großbuchstaben geschrieben sein");
        }
        if (name.length() > 100) {
            throw new ValidationException("Rolle ist zu lang!");
        }
        Pattern pattern = Pattern.compile("^[A-ZÖÜÄ\\s\\.\\-/]+$");
        Matcher matcher = pattern.matcher(name);

        if (!(matcher.matches())) {
            throw new ValidationException("Invalider Rollenname!");
        }
    }
}
