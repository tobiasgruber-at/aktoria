package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

public class UserRegistrationDto {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;

    public UserRegistrationDto(String name, String lastName, String email, String password) {
        this.firstName = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public UserRegistrationDto() {
        this.lastName = "";
        this.firstName = "";
        this.email = "";
        this.password = "";
    }

    public String getFirstName() {
        return firstName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
