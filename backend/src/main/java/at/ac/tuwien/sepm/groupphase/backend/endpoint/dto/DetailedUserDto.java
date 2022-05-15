package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
