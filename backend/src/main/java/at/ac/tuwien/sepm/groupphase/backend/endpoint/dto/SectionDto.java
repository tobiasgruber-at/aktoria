package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Line;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Section Data Transfer Object.
 * <br>
 * This DTO holds references to an ID, a name, the starting line, the ending line.
 *
 * @author Julia Bernold
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SectionDto {
    private Long id;
    private String name;
    private Line start;
    private Line end;
}
