package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailedUserDto extends SimpleUserDto {
    private String password;

    public DetailedUserDto(Long id, String firstName, String lastName, String email, String password, Boolean verified) {
        super(id, firstName, lastName, email, verified);
        this.password = password;
    }
}
