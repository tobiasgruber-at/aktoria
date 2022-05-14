package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.service.parsing.line.Line;

import java.util.List;
import java.util.Objects;

/**
 * Simple Line UserEndpoint.path.
 * <br>
 * A simple line DTO only contains the role names, the content
 * a raw version as well as the conflict type.
 *
 * @author Simon Josef Kreuzpointner
 */
public class SimpleLineDto {

    private Long index;
    private List<SimpleRoleDto> roles;
    private String content;
    private boolean active = true;
    private Line.ConflictType conflictType;

    public SimpleLineDto(Long index, List<SimpleRoleDto> roles, String content, boolean active, Line.ConflictType conflictType) {
        this.index = index;
        this.roles = roles;
        this.content = content;
        this.active = active;
        this.conflictType = conflictType;
    }

    public SimpleLineDto() {
        this(null, null, null, true, null);
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public List<SimpleRoleDto> getRoles() {
        return roles;
    }

    public void setRoles(List<SimpleRoleDto> roles) {
        this.roles = roles;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Line.ConflictType getConflictType() {
        return conflictType;
    }

    public void setConflictType(Line.ConflictType conflictType) {
        this.conflictType = conflictType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimpleLineDto that = (SimpleLineDto) o;
        return active == that.active && Objects.equals(index, that.index) && Objects.equals(roles, that.roles) && Objects.equals(content, that.content) && conflictType == that.conflictType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, roles, content, active, conflictType);
    }
}
