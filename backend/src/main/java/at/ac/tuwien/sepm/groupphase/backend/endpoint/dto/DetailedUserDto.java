package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.util.Objects;

public class DetailedUserDto extends SimpleUserDto {
    private String password;

    public DetailedUserDto(Long id, String firstName, String lastName, String email, String password, Boolean verified) {
        super(id, firstName, lastName, email, verified);
        this.password = password;
    }

    public DetailedUserDto() {
        super();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
        return Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), password);
    }
}
