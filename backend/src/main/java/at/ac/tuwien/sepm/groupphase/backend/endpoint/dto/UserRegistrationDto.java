package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

public class UserRegistrationDto {
    private final String name;
    private final String email;
    private final String password;

    public UserRegistrationDto(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public UserRegistrationDto() {
        this.name = "";
        this.email = "";
        this.password = "";
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
