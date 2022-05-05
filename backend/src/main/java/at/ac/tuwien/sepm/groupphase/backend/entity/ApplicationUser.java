package at.ac.tuwien.sepm.groupphase.backend.entity;

//TODO: replace this class with a correct ApplicationUser Entity implementation
public class ApplicationUser {

    private Long id;
    private String name;
    private String email;
    private String password;
    private Boolean verified;

    public ApplicationUser() {
    }

    public ApplicationUser(Long id, String name, String email, String password, Boolean verified) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.verified = verified;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }
}
