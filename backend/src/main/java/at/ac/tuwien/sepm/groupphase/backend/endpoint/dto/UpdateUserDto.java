package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.Data;

@Data
public class UpdateUserDto extends DetailedUserDto {
    private String newPassword;

    public UpdateUserDto(Long id, String firstName, String lastName, String email, String oldPassword, String newPassword, Boolean verified) {
        super(id, firstName, lastName, email, oldPassword, verified);
        this.newPassword = newPassword;
    }

    public UpdateUserDto() {
        super();
        this.newPassword = null;
    }
}
