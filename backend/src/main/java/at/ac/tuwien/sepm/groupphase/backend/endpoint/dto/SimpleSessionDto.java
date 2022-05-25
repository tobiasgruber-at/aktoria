package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Simple Session Data Transfer Object.
 * <br>
 * This DTO is intended for creating new sessions with
 * the least possible data.
 *
 * @author Marvin Flandorfer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleSessionDto {
    private Long sectionId;
    private Long roleId;
}
