package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

public class PasswordChangeDto {
    private final String oldPassword;
    private final String newPassword;

    public PasswordChangeDto(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
