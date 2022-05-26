package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SectionDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Section;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

/**
 * Describes a section mapper.
 *
 * @author Julia Bernold
 */

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface SectionMapper {
    SectionDto sectionToSectionDto(Section section);

    Section sectionDtoToSection(SectionDto sectionDto);
}
