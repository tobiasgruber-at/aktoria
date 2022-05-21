package at.ac.tuwien.sepm.groupphase.backend.validation;

import org.springframework.stereotype.Component;

@Component
public interface RoleValidation {

    void validateRoleName(String name);
}
