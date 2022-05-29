package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Section Data Transfer Object.
 * <br>
 * This DTO holds references to an ID, a name, the owner, the starting line,
 * the ending line and the sessions this section has been used in.
 *
 * @author Julia Bernold
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SectionDto {
    private Long id;
    private String name;
    private SimpleUserDto owner;
    private LineDto startLine;
    private LineDto endLine;
    private List<SessionDto> sessions;
}
