package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SessionDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Session;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

/**
 * Describes a session mapper.
 *
 * @author Julia Bernold
 */
@Mapper(componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface SessionMapper {
    SessionDto sessionToSessionDto(Session session);

    List<SessionDto> setOfSessionToListOfSessionDto(Set<Session> sessions);
}
