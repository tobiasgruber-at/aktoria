package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.service.parsing.line.Line;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Blob;
import java.util.List;

/**
 * Line Data Transfer Object.
 * <br>
 * In contrast to the simple line DTO this DTO holds references to an id,
 * the index of the line, the page it belongs to as well as an audio recording
 * and a reference to the user, who recorded that audio snippet.
 * <br>
 * This DTO is intended to be used for already stored lines.
 *
 * @author Simon Josef Kreuzpointner
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LineDto {
    private Long id;
    private Long index;
    private Long pageId;
    private List<RoleDto> roles;
    private String content;
    private Blob audio;
    private SimpleUserDto recordedBy;
    private boolean active;
    private Line.ConflictType conflictType;
}
