package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ScriptDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ScriptPreviewDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleScriptDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleUserDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Script;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.script.ParsedScript;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * Describes a script mapper.
 *
 * @author Simon Josef Kreuzpointner
 */

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {RoleMapper.class, PageMapper.class})
public interface ScriptMapper {
    SimpleScriptDto parsedScriptToSimpleScriptDto(ParsedScript parsedScript, String name);

    @Mappings({
        @Mapping(target = "owner", source = "owner"),
        @Mapping(target = "id", source = "id")
    })
    ScriptDto simpleScriptDtoToScriptDto(SimpleScriptDto simpleScriptDto, Long id, SimpleUserDto owner);

    @Mapping(target = "owner", source = "owner")
    ScriptPreviewDto scriptToScriptPreviewDto(Script script, boolean owner);

    ScriptDto scriptToScriptDto(Script script);
}
