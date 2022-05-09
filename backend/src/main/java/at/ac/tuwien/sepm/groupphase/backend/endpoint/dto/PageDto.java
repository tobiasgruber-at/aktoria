package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.util.List;

/**
 * Page Data Access Object.
 * <br>
 * In contrast to the simple page DTO this DTO holds a reference
 * to an id, the script it belongs to as well as the index of a page.
 * <br>
 * This DTO is intended for the use of already stored pages.
 *
 * @author Simon Josef Kreuzpointner
 */
public class PageDto {

    private final Long id;
    private final Long scriptId;
    private final Long index;
    private final List<LineDto> lines;

    public PageDto(Long id, Long scriptId, Long index, List<LineDto> lines) {
        this.id = id;
        this.scriptId = scriptId;
        this.index = index;
        this.lines = lines;
    }

    public Long getId() {
        return id;
    }

    public Long getScriptId() {
        return scriptId;
    }

    public Long getIndex() {
        return index;
    }

    public List<LineDto> getLines() {
        return lines;
    }
}
