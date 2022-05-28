package at.ac.tuwien.sepm.groupphase.backend.service;


import org.springframework.stereotype.Component;

/**
 * Describes a session service component.
 *
 * @author Simon Josef Kreuzpointner
 */
@Component
public interface SessionService {
    /**
     * Sets the deprecated flag of every session,
     * that is associated with the script id.
     *
     * @param id the script id
     */
    void deprecateAffected(Long id);

    /**
     * Sets the deprecated flag of the given session.
     *
     * @param id the session id
     */
    void deprecate(Long id);
}
