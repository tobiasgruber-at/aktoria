package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * User-Login Data Transfer Object.
 * <br>
 * This represents a user for login.
 *
 * @author Nikolaus Peter
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDto {
    @NotNull(message = "Email must not be null")
    @Email
    private String email;
    @NotNull(message = "Password must not be null")
    private String password;
}
