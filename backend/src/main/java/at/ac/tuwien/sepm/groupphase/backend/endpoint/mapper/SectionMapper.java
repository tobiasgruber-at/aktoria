package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SectionDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Section;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * Describes a section mapper.
 *
 * @author Julia Bernold
 */

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = SessionMapper.class)
public interface SectionMapper {

    @Mappings({
        @Mapping(target = "owner", source = "owner.id"),
        @Mapping(target = "endLine", source = "endLine.id"),
        @Mapping(target = "startLine", source = "startLine.id")
    })
    SectionDto sectionToSectionDto(Section section);
}
