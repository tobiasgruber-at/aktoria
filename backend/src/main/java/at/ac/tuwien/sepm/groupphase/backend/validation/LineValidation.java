package at.ac.tuwien.sepm.groupphase.backend.validation;

import org.springframework.stereotype.Component;

import java.util.List;

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

    /**
     * Validates input for line roles.
     *
     * @param ids list of role ids.
     */
    void validateRoleIdsInput(List<Long> ids);

    /**
     * Validates the audio for a line.
     * <br>
     * The audio string for a line is considered valid if it has
     * a length of less or equal to 10485760, making the line
     * less or equal to 10MB when using ASCII characters.
     *
     * @param audio the audio of a line.
     */
    void validateAudio(String audio);
}
