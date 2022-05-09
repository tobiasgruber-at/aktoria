package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.util.List;
import java.util.Objects;

/**
 * Simple Page Data Access Object.
 * <br>
 * A simple page DAO only contains the line that start on
 * this page.
 *
 * @author Simon Josef Kreuzpointner
 */
public class SimplePageDto {
    private List<SimpleLineDto> lines;
    private Long index;

    public SimplePageDto(List<SimpleLineDto> lines, Long index) {
        this.lines = lines;
        this.index = index;
    }

    public SimplePageDto() {
        this(null, null);
    }

    public List<SimpleLineDto> getLines() {
        return lines;
    }

    public void setLines(List<SimpleLineDto> lines) {
        this.lines = lines;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimplePageDto that = (SimplePageDto) o;
        return Objects.equals(lines, that.lines) && Objects.equals(index, that.index);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lines, index);
    }
}
