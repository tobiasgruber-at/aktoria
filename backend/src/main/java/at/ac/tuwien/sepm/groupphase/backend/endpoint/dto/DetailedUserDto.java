package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

public class DetailedUserDto extends SimpleUserDto {
    private final String password;

    public DetailedUserDto(Long id, String firstName, String lastName, String email, String password, Boolean verified) {
        super(id, firstName, lastName, email, verified);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
