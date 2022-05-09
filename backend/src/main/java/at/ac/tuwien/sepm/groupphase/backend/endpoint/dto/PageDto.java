package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.util.List;
import java.util.Objects;

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

    private Long id;
    private Long scriptId;
    private Long index;
    private List<LineDto> lines;

    public PageDto(Long id, Long scriptId, Long index, List<LineDto> lines) {
        this.id = id;
        this.scriptId = scriptId;
        this.index = index;
        this.lines = lines;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getScriptId() {
        return scriptId;
    }

    public void setScriptId(Long scriptId) {
        this.scriptId = scriptId;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public List<LineDto> getLines() {
        return lines;
    }

    public void setLines(List<LineDto> lines) {
        this.lines = lines;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PageDto pageDto = (PageDto) o;
        return Objects.equals(id, pageDto.id) && Objects.equals(scriptId, pageDto.scriptId) && Objects.equals(index, pageDto.index) && Objects.equals(lines, pageDto.lines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, scriptId, index, lines);
    }
}
