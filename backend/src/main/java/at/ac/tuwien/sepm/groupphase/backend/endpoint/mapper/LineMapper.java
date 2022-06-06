package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LineDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleLineDto;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.line.Line;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * Describes a line mapper.
 *
 * @author Simon Josef Kreuzpointner
 */
@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = { RoleMapper.class })
public interface LineMapper {

    SimpleLineDto lineToSimpleLineDto(Line line);

    List<SimpleLineDto> listOfLinetoListOfSimpleLineDto(List<Line> lines);

    @Mappings({
        @Mapping(target = "roles", source = "spokenBy"),
        @Mapping(target = "pageId", source = "page.id")
    })
    LineDto lineToLineDto(at.ac.tuwien.sepm.groupphase.backend.entity.Line line);
}
