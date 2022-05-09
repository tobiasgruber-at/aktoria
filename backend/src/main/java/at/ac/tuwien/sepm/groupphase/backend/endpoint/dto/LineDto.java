package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.service.parsing.line.Line;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Line Data Access Object.
 * <br>
 * In contrast to the simple line DTO this DTO holds references to an id,
 * the index of the line, the page it belongs to as well as an audio recording
 * and a reference to the user, who recorded that audio snippet.
 * <br>
 * This DTO is intended to be used for already stored lines.
 *
 * @author Simon Josef Kreuzpointner
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LineDto {

    private Long id;
    private Long index;
    private Long pageId;
    private List<RoleDto> roles;
    private String content;
    private String audio;
    private SimpleUserDto recordedBy;
    private boolean active;
    private Line.ConflictType conflictType;

    public LineDto(Long id, Long index, Long pageId, List<RoleDto> roles, String content, String audio, SimpleUserDto recordedBy, boolean active, Line.ConflictType conflictType) {
        this.id = id;
        this.index = index;
        this.pageId = pageId;
        this.roles = roles;
        this.content = content;
        this.audio = audio;
        this.recordedBy = recordedBy;
        this.active = active;
        this.conflictType = conflictType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public Long getPageId() {
        return pageId;
    }

    public void setPageId(Long pageId) {
        this.pageId = pageId;
    }

    public List<RoleDto> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDto> roles) {
        this.roles = roles;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public SimpleUserDto getRecordedBy() {
        return recordedBy;
    }

    public void setRecordedBy(SimpleUserDto recordedBy) {
        this.recordedBy = recordedBy;
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
}
