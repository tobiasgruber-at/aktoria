package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Line;
import at.ac.tuwien.sepm.groupphase.backend.entity.Session;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

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
    private User owner;
    private Line start;
    private Line end;
    private Set<Session> sessions;
}
