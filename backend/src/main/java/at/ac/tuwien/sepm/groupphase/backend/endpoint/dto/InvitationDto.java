package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Invitaion Data Transfer Object.
 * <br>
 * This represents an invitation to participate of a script.
 *
 * @author Nikolaus Peter
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvitationDto {
    private Long id_script;
    private String email;
    private String message;
}
