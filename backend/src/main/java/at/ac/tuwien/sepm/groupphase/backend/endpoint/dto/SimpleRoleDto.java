package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Role Data Transfer Object.
 * <br>
 * This represents a role with its simplest data.
 *
 * @author Simon Josef Kreuzpointner
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleRoleDto {
    private String name;
    private SimpleColorDto color;
}
