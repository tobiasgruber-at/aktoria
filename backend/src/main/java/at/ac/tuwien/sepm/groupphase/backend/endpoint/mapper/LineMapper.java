package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleLineDto;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.line.Line;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Describes a line mapper.
 *
 * @author Simon Josef Kreuzpointner
 */
@Mapper(uses = { RoleMapper.class })
public interface LineMapper {
    LineMapper INSTANCE = Mappers.getMapper(LineMapper.class);

    SimpleLineDto lineToSimpleLineDto(Line line);

    List<SimpleLineDto> listOfLinetoListOfSimpleLineDto(List<Line> lines);
}
