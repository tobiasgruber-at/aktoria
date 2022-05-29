package at.ac.tuwien.sepm.groupphase.backend.validation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SectionDto;
import org.springframework.stereotype.Component;

/**
 * Validation for Section.
 *
 * @author Julia Bernold
 */
@Component
public interface SectionValidation {
    /**
     * Validates input when creating a new section.
     *
     * @param sectionDto the section to be validated
     */
    void validateCreateSection(SectionDto sectionDto);

    /**
     * Validates if the logged-in user is the owner of the section.
     *
     * @param owner the ID of the owner of the section
     */
    void ownerLoggedIn(Long owner);
}
