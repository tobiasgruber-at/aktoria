package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SessionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleSessionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UpdateSessionDto;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

/**
 * Describes a session service component.
 *
 * @author Marvin Flandorfer
 */
@Service
public interface SessionService {

    /**
     * Starts a new session and saves it in the data storage.
     *
     * @param simpleSessionDto the session to be started
     * @return the started session
     */
    SessionDto save(SimpleSessionDto simpleSessionDto);

    /**
     * Updates a session in the data storage.
     *
     * @param updateSessionDto the session that contains the changes
     * @param id the id of the session
     * @return the updated session
     */
    SessionDto update(UpdateSessionDto updateSessionDto, Long id);

    /**
     * Finishes a session in the data storage.
     *
     * @param id the id of the session
     * @return the finished session
     */
    SessionDto finish(Long id);

    /**
     * Gets a session corresponding to the given id.
     *
     * @param id the id of the session
     * @return the session found in the data storage
     */
    SessionDto findById(Long id);

    /**
     * Gets all sessions for a user.
     *
     * @return all sessions found in the data storage for the user.
     */
    Stream<SessionDto> findAll();
}
