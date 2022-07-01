package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Password Data Transfer Object.
 * <br>
 * This is used for changing the password of a user.
 *
 * @author Tobias Gruber
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeDto {
    private String token;
    private String oldPassword;
    private String newPassword;
}
