package at.ac.tuwien.sepm.groupphase.backend.validation;

import org.springframework.stereotype.Component;

/**
 * Validation for Roles.
 *
 * @author Luke Nemeskeri
 */
@Component
public interface RoleValidation {

    /**
     * Validates the name for a role.
     *
     * @param name the name of the role
     */
    void validateRoleName(String name);
}
