package at.ac.tuwien.sepm.groupphase.backend.validation;

import org.springframework.stereotype.Component;

/**
 * Validation for Line.
 *
 * @author Simon Josef Kreuzpointner
 */
@Component
public interface LineValidation {
    /**
     * Validates input for a line content.
     *
     * @param content the new content of a line.
     */
    void validateContentInput(String content);
}
