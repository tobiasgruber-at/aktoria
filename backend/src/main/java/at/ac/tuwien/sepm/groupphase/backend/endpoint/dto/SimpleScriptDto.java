package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.PageMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.RoleMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.script.ParsedScript;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.List;

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
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class SimpleScriptDto {
    private String name;
    private List<SimplePageDto> pages;
    private List<SimpleRoleDto> roles;

    @Deprecated
    public static SimpleScriptDto of(ParsedScript parsedScript, String name) {
        List<SimpleRoleDto> roles = Mappers.getMapper(RoleMapper.class).listOfStringToListOfSimpleRoleDto(parsedScript.getRoles());
        List<SimplePageDto> pages = Mappers.getMapper(PageMapper.class).listOfPageToListOfSimplePageDto(parsedScript.getPages());
        return new SimpleScriptDto(name, pages, roles);
    }
}
