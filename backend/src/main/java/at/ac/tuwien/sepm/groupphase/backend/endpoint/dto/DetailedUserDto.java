package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

public class DetailedUserDto extends SimpleUserDto {
    private final String password;

    public DetailedUserDto(Long id, String name, String email, String password, Boolean verified) {
        super(id, name, email, verified);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}

