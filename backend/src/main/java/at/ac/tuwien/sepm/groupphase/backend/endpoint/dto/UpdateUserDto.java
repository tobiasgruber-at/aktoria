package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
