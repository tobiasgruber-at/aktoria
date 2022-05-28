package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Line;
import at.ac.tuwien.sepm.groupphase.backend.entity.Role;
import at.ac.tuwien.sepm.groupphase.backend.entity.Section;
import at.ac.tuwien.sepm.groupphase.backend.enums.AssessmentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Session Data Transfer Object.
 * <br>
 * This DTO holds references to an ID, start and end time,
 * a self assessment, a boolean describing whether the session is deprecated,
 * a description of the coverage, the practiced section, current Line and role.
 *
 * @author Julia Bernold
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private AssessmentType selfAssessment;
    private Boolean deprecated;
    private Double coverage;
    private Section section;
    private Line currentLine;
    private Role role;
}
