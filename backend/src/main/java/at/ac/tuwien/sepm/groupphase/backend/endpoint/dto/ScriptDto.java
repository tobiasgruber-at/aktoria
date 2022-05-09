package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.util.List;
import java.util.Objects;

/**
 * Script Data Access Object.
 * <br>
 * In contrast to the simple script DTO this DTO is intended
 * for already stored scripts.
 *
 * @author Simon Josef Kreuzpointner
 */
public class ScriptDto {

    private Long id;
    private String name;
    private SimpleUserDto owner;
    private List<PageDto> pages;

    public ScriptDto(Long id, String name, SimpleUserDto owner, List<PageDto> pages) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.pages = pages;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SimpleUserDto getOwner() {
        return owner;
    }

    public void setOwner(SimpleUserDto owner) {
        this.owner = owner;
    }

    public List<PageDto> getPages() {
        return pages;
    }

    public void setPages(List<PageDto> pages) {
        this.pages = pages;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ScriptDto scriptDto = (ScriptDto) o;
        return Objects.equals(id, scriptDto.id) && Objects.equals(name, scriptDto.name) && Objects.equals(owner, scriptDto.owner) && Objects.equals(pages, scriptDto.pages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, owner, pages);
    }
}
