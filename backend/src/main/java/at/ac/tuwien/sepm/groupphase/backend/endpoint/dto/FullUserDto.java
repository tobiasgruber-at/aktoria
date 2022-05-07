package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

public class FullUserDto extends DetailedUserDto {
    String newPassword;

    public FullUserDto(Long id, String firstName, String lastName, String email, String oldPassword, String newPassword, Boolean verified) {
        super(id, firstName, lastName, email, oldPassword, verified);
        this.newPassword = newPassword;

    }

    public FullUserDto() {
        super();
        this.newPassword = "";
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getOldPassword() {
        return super.getPassword();
    }
}
