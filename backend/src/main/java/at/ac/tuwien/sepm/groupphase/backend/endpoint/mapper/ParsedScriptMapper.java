package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.StagedScriptDto;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.script.ParsedScript;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Describes a parsed script mapper.
 *
 * @author Simon Josef Kreuzpointner
 */
@Mapper
public interface ParsedScriptMapper {
    ParsedScriptMapper INSTANCE = Mappers.getMapper(ParsedScriptMapper.class);

    StagedScriptDto parsedScriptToScriptDto(ParsedScript parsedScript);
}
