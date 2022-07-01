package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Line Data Transfer Object.
 * <br>
 * This represents a line for updating it.
 *
 * @author Simon Josef Kreuzpointner
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateLineDto {
    private String content;
    private Boolean active;
    private List<Long> roleIds;
    private String audio;
}
