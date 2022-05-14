package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ScriptDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleScriptDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleUserDto;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.script.ParsedScript;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {RoleMapper.class, PageMapper.class})
public interface SimpleScriptMapper {
    SimpleScriptDto parsedScriptToSimpleScriptDto(ParsedScript parsedScript, String name);

    @Mapping(target = "owner", source = "owner")
    ScriptDto simpleScriptDtoToScriptDto(SimpleScriptDto simpleScriptDto, Long id, SimpleUserDto owner);
}
