package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SessionDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Session;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface SessionMapper {

    @Mappings({
        @Mapping(target = "sectionId", source = "section.id"),
        @Mapping(target = "roleId", source = "role.id"),
        @Mapping(target = "currentLineId", source = "currentLine.id")
    })
    SessionDto sessionToSessionDto(Session session);
}
