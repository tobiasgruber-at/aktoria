package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleLineDto;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.line.Line;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Describes a line mapper.
 *
 * @author Simon Josef Kreuzpointner
 */
@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {RoleMapper.class})
public interface LineMapper {

    SimpleLineDto lineToSimpleLineDto(Line line);

    List<SimpleLineDto> listOfLinetoListOfSimpleLineDto(List<Line> lines);
}
