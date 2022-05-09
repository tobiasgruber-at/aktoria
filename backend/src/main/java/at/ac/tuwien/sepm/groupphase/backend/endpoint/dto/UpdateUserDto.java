package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.util.Objects;

public class UpdateUserDto extends DetailedUserDto {
    String newPassword;

    public UpdateUserDto(Long id, String firstName, String lastName, String email, String oldPassword, String newPassword, Boolean verified) {
        super(id, firstName, lastName, email, oldPassword, verified);
        this.newPassword = newPassword;
    }

    public UpdateUserDto() {
        super();
        this.newPassword = null;
    }

    public String getNewPassword() {
        return newPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UpdateUserDto that = (UpdateUserDto) o;
        return Objects.equals(newPassword, that.newPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(newPassword);
    }
}
