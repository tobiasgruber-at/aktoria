package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.service.parsing.page.Page;

import java.util.List;
import java.util.Objects;

public class StagedScriptDto {

    List<Page> pages;
    List<String> roles;

    public StagedScriptDto(List<Page> pages, List<String> roles) {
        this.pages = pages;
        this.roles = roles;
    }

    public List<Page> getPages() {
        return pages;
    }

    public void setPages(List<Page> pages) {
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
