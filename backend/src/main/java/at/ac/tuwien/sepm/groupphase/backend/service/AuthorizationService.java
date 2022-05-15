package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.exception.UnauthorizedException;

public interface AuthorizationService {
    /**
     * Checks if the client is logged in as the user with the given id.
     *
     * @param id the checked id
     * @return true if logged in as user with this id false if not
     */
    boolean isLoggedInAs(Long id);

    /**
     * Checks if the client is logged in as the user with the given email.
     *
     * @param email the checked email
     * @return true if logged in as user with this email false if not
     */
    boolean isLoggedInAs(String email);

    /**
     * Gets the logged-in user.
     *
     * @return a user entity, or null if not logged in
     */
    User getLoggedInUser();

    /**
     * Checks if the client is logged in as the user with the given email,
     * or is an Admin.
     *
     * @param email the user email
     * @throws UnauthorizedException if not logged in as that user or admin
     */
    void checkBasicAuthorization(String email);

    /**
     * Checks if the client is logged in as the user with the given id,
     * or is an Admin.
     *
     * @param id the user id
     * @throws UnauthorizedException if not logged in as that user or admin
     */
    void checkBasicAuthorization(Long id);
}
