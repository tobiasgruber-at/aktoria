package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SectionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleSectionDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Section;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * Describes a section mapper.
 *
 * @author Julia Bernold
 */
@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {SessionMapper.class, UserMapper.class, LineMapper.class})
public interface SectionMapper {

    SectionDto sectionToSectionDto(Section section);

    List<SectionDto> listOfSectionToListOfSectionDto(List<Section> sections);

    @Mappings({
        @Mapping(target = "ownerId", source = "owner.id"),
        @Mapping(target = "startLineId", source = "startLine.id"),
        @Mapping(target = "endLineId", source = "endLine.id")
    })
    SimpleSectionDto sectionToSimpleSectionDto(Section section);
}
