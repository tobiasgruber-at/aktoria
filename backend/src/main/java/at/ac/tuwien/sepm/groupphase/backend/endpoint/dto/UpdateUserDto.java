package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * User Data Transfer Object.
 * <br>
 * This represents a user to update.
 *
 * @author Luke Nemeskeri
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String oldPassword;
    private String newPassword;
    private Boolean verified;
}
