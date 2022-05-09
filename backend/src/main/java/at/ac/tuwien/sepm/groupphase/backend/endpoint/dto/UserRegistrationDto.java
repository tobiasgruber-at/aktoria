package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.util.Objects;

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
        this.lastName = null;
        this.firstName = null;
        this.email = null;
        this.password = null;
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

    public String getLastName() {
        return lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserRegistrationDto that = (UserRegistrationDto) o;
        return Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(email, that.email) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, password);
    }
}
