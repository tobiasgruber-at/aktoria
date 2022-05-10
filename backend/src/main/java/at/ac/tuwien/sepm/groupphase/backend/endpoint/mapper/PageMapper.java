package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimplePageDto;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.page.Page;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Describes a page mapper.
 *
 * @author Simon Josef Kreuzpointner
 */
@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = { LineMapper.class })
public interface PageMapper {
    List<SimplePageDto> listOfPageToListOfSimplePageDto(List<Page> pages);
}
