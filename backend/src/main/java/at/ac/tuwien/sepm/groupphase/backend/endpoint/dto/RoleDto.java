package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.awt.Color;

/**
 * Role Data Access Object.
 * <br>
 * This represents a role of a script.
 *
 * @author Simon Josef Kreuzpointner
 */
public class RoleDto {
    private final Long id;
    private final String name;
    private final Long scriptId;
    private final Color color;

    public RoleDto(Long id, String name, Long scriptId, Color color) {
        this.id = id;
        this.name = name;
        this.scriptId = scriptId;
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getScriptId() {
        return scriptId;
    }

    public Color getColor() {
        return color;
    }
}
