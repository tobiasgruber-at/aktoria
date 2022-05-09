package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.util.Objects;

public class DetailedUserDto extends SimpleUserDto {
    private String passwordHash;

    public DetailedUserDto(Long id, String firstName, String lastName, String email, String password, Boolean verified) {
        super(id, firstName, lastName, email, verified);
        this.passwordHash = password;
    }

    public DetailedUserDto() {
        super();
        this.passwordHash = null;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String password) {
        this.passwordHash = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        DetailedUserDto that = (DetailedUserDto) o;
        return Objects.equals(passwordHash, that.passwordHash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), passwordHash);
    }
}
