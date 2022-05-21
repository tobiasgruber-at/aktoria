package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Join Data Transfer Object.
 * <br>
 * This represents joining a script.
 *
 * @author Nikolaus Peter
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinDto {
    Long id;
    String token;
}
