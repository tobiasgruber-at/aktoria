package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

public class SimpleUserDto {
    Long id;
    String firstName;
    String lastName;
    String email;
    Boolean verified;

    public SimpleUserDto(Long id, String firstName, String lastName, String email, Boolean verified) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.verified = verified;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Boolean getVerified() {
        return verified;
    }
}
