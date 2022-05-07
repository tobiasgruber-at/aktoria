package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.service.parsing.line.Line;

/**
 * Line Data Access Object.
 *
 * @author Simon Josef Kreuzpointner
 */
public class LineDto {

    private final Long id;
    private final Long index;
    private final Long pageId;
    private final RoleDto role;
    private final String content;
    private final String audio;
    private final SimpleUserDto recordedBy;
    private final boolean active;
    private final Line.ConflictType conflictType;

    public LineDto(Long id, Long index, Long pageId, RoleDto role, String content, String audio, SimpleUserDto recordedBy, boolean active, Line.ConflictType conflictType) {
        this.id = id;
        this.index = index;
        this.pageId = pageId;
        this.role = role;
        this.content = content;
        this.audio = audio;
        this.recordedBy = recordedBy;
        this.active = active;
        this.conflictType = conflictType;
    }

    public Long getId() {
        return id;
    }

    public Long getIndex() {
        return index;
    }

    public Long getPageId() {
        return pageId;
    }

    public RoleDto getRole() {
        return role;
    }

    public String getContent() {
        return content;
    }

    public String getAudio() {
        return audio;
    }

    public SimpleUserDto getRecordedBy() {
        return recordedBy;
    }

    public boolean isActive() {
        return active;
    }

    public Line.ConflictType getConflictType() {
        return conflictType;
    }
}
