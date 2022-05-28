package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SessionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleSessionDto;
import org.springframework.stereotype.Service;

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
     * @param sessionDto the session that contains the changes
     * @param id the id of the session
     * @return the updated session
     */
    SessionDto update(SessionDto sessionDto, Long id);

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
}
