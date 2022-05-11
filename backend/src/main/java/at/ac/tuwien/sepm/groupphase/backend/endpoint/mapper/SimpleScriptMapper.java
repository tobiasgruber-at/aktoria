package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleScriptDto;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.script.ParsedScript;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {RoleMapper.class, PageMapper.class})
public interface SimpleScriptMapper {
    SimpleScriptDto parsedScriptToSimpleScriptDto(ParsedScript parsedScript, String name);
}
