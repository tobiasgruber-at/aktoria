package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Role Data Transfer Object.
 * <br>
 * This represents a role of a script.
 *
 * @author Simon Josef Kreuzpointner
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {
    private Long id;
    private Long scriptId;
    private String name;
    private SimpleColorDto color;
}
