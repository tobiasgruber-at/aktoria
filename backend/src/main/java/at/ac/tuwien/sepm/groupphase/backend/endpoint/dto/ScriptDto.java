package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.util.List;

/**
 * Script Data Access Object.
 * <br>
 * In contrast to the simple script DTO this DTO is intended
 * for already stored scripts.
 *
 * @author Simon Josef Kreuzpointner
 */
public class ScriptDto {

    private final Long id;
    private final String name;
    private final SimpleUserDto owner;
    private final List<LineDto> lines;
    private final List<RoleDto> roles;

    public ScriptDto(Long id, String name, SimpleUserDto owner, List<LineDto> lines, List<RoleDto> roles) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.lines = lines;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public SimpleUserDto getOwner() {
        return owner;
    }

    public List<LineDto> getLines() {
        return lines;
    }

    public List<RoleDto> getRoles() {
        return roles;
    }
}
