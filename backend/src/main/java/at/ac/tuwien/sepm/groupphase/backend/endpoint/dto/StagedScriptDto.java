package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.util.List;
import java.util.Objects;

/**
 * Staged Script Data Access Object.
 * <br>
 * This DAO only includes simple data access objects.
 * The roles are only saved as role names.
 *
 * @author Simon Josef Kreuzpointner
 */
public class StagedScriptDto {

    private List<SimplePageDto> pages;
    private List<String> roles;

    public StagedScriptDto(List<SimplePageDto> pages, List<String> roles) {
        this.pages = pages;
        this.roles = roles;
    }

    public StagedScriptDto() {
        this(null, null);
    }

    public List<SimplePageDto> getPages() {
        return pages;
    }

    public void setPages(List<SimplePageDto> pages) {
        this.pages = pages;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StagedScriptDto that = (StagedScriptDto) o;
        return Objects.equals(pages, that.pages) && Objects.equals(roles, that.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pages, roles);
    }
}
