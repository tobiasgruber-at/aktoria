package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

public class SimpleUserDto {
    Long id;
    String name;
    String email;
    Boolean verified;

    public SimpleUserDto(Long id, String name, String email, Boolean verified) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.verified = verified;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Boolean getVerified() {
        return verified;
    }
}
