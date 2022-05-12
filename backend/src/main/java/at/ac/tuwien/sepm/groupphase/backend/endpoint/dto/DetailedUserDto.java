package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailedUserDto extends SimpleUserDto {
    private String passwordHash;

    public DetailedUserDto(Long id, String firstName, String lastName, String email, String passwordHash, Boolean verified) {
        super(id, firstName, lastName, email, verified);
        this.passwordHash = passwordHash;
    }
}
