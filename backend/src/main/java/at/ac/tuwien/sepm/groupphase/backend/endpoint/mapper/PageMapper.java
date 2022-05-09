package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimplePageDto;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.page.Page;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Describes a page mapper.
 *
 * @author Simon Josef Kreuzpointner
 */
@Mapper(uses = { RoleMapper.class })
public interface PageMapper {
    PageMapper INSTANCE = Mappers.getMapper(PageMapper.class);

    List<SimplePageDto> listOfPageToListOfSimplePageDto(List<Page> pages);
}
