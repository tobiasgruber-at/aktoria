package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.enums.AssessmentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Session Data Transfer Object.
 * <br>
 * This represents a session to update.
 *
 * @author Marvin Flandorfer
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSessionDto {
    private Boolean deprecated;
    private AssessmentType selfAssessment;
    private Long currentLineId;
}
