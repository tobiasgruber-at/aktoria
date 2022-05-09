package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.PageMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.RoleMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.script.ParsedScript;

import java.util.List;
import java.util.Objects;

/**
 * Staged Script Data Access Object.
 * <br>
 * This DAO only includes simple data access objects.
 * The roles are only saved as role names.
 * <br>
 * The staged script DTO is intended for newly uploaded and parsed
 * scripts, that are not yet stored.
 *
 * @author Simon Josef Kreuzpointner
 */
public class SimpleScriptDto {

    private String name;
    private List<SimplePageDto> pages;
    private List<SimpleRoleDto> roles;

    public SimpleScriptDto(String name, List<SimplePageDto> pages, List<SimpleRoleDto> roles) {
        this.name = name;
        this.pages = pages;
        this.roles = roles;
    }

    public SimpleScriptDto() {
        this(null, null, null);
    }

    public static SimpleScriptDto of(ParsedScript parsedScript, String name) {
        List<SimpleRoleDto> roles = RoleMapper.INSTANCE.listOfStringToListOfSimpleRoleDto(parsedScript.getRoles());
        List<SimplePageDto> pages = PageMapper.INSTANCE.listOfPageToListOfSimplePageDto(parsedScript.getPages());
        return new SimpleScriptDto(name, pages, roles);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SimplePageDto> getPages() {
        return pages;
    }

    public void setPages(List<SimplePageDto> pages) {
        this.pages = pages;
    }

    public List<SimpleRoleDto> getRoles() {
        return roles;
    }

    public void setRoles(List<SimpleRoleDto> roles) {
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
        SimpleScriptDto that = (SimpleScriptDto) o;
        return Objects.equals(name, that.name) && Objects.equals(pages, that.pages) && Objects.equals(roles, that.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, pages, roles);
    }
}
