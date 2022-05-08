package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.util.List;
import java.util.Objects;

public class SimplePageDto {
    private List<SimpleLineDto> lines;

    public SimplePageDto(List<SimpleLineDto> lines) {
        this.lines = lines;
    }

    public SimplePageDto() {
        this(null);
    }

    public List<SimpleLineDto> getLines() {
        return lines;
    }

    public void setLines(List<SimpleLineDto> lines) {
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
        SimplePageDto that = (SimplePageDto) o;
        return Objects.equals(lines, that.lines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lines);
    }
}
