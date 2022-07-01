package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Script-Update Data Transfer Object.
 * <br>
 * This represents a script to update.
 *
 * @author Simon Josef Kreuzpointner
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateScriptDto {
    @Pattern(regexp = "^(?!\\s*$).+")
    @Size(min = 1, max = 255)
    private String name;
}
