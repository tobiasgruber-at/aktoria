package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.enums.AssessmentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Session Data Transfer Object.
 *
 * This DTO is intended for transferring all
 * available Data of a session.
 *
 * @author Marvin Flandorfer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionDto {
    private Long id;
    private Double coverage;
    private Boolean deprecated;
    private LocalDateTime start;
    private LocalDateTime end;
    private AssessmentType selfAssessment;
    private Long sectionId;
    private Long roleId;
    private Long currentLine;
}
