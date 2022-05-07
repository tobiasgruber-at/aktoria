package at.ac.tuwien.sepm.groupphase.backend.entity;

public class Script {

    private Long id;
    private String name;
    private ApplicationUser owner;

    public Script(Long id, String name, ApplicationUser owner) {
        this.id = id;
        this.name = name;
        this.owner = owner;
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

    public ApplicationUser getOwner() {
        return owner;
    }

    public void setOwner(ApplicationUser owner) {
        this.owner = owner;
    }
}
