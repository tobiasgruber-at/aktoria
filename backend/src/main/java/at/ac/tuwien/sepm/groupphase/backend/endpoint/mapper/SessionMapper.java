package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SessionDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Session;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = RoleMapper.class)
public interface SessionMapper {

    @Mappings({
        @Mapping(target = "sectionId", source = "section.id"),
        @Mapping(target = "currentLineIndex", source = "currentLine.index")
    })
    SessionDto sessionToSessionDto(Session session);

    @Mapping(target = "sectionId", source = "section.id")
    List<SessionDto> setOfSessionToListOfSessionDto(Set<Session> sessionSet);
}
