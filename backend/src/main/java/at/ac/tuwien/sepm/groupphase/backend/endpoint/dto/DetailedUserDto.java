package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User Data Transfer Object.
 * <br>
 * This represents a user with all its data.
 *
 * @author Luke Nemeskeri
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailedUserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String passwordHash;
    private Boolean verified;
}
