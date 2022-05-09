package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.service.parsing.line.Line;

import java.util.List;
import java.util.Objects;

/**
 * Simple Line Data Access Object.
 * <br>
 * A simple line DAO only contains the role names, the content
 * a raw version as well as the conflict type.
 *
 * @author Simon Josef Kreuzpointner
 */
public class SimpleLineDto {

    private List<String> roles;
    private String content;
    private String raw;
    private Line.ConflictType conflictType;

    public SimpleLineDto(List<String> roles, String content, String raw, Line.ConflictType conflictType) {
        this.roles = roles;
        this.content = content;
        this.raw = raw;
        this.conflictType = conflictType;
    }

    public SimpleLineDto() {
        this(null, null, null, null);
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
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
        return Objects.equals(roles, that.roles) && Objects.equals(content, that.content) && Objects.equals(raw, that.raw) && conflictType == that.conflictType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(roles, content, raw, conflictType);
    }
}
