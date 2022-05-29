package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Simple Section Data Transfer Object.
 * <br>
 * This DTO holds references to an ID, a name, the owner, the starting line,
 * the ending line and the sessions this section has been used in.
 *
 * @author Simon Josef Kreuzpointner
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleSectionDto {
    private String name;
    private Long ownerId;
    private Long startLineId;
    private Long endLineId;
    private List<Long> sessionIds;
}
