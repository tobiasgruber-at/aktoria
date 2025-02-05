package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.exception.UnauthorizedException;

/**
 * Interface for Authorization.
 *
 * @author Nikolaus Peter
 */
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
     * or is an admin.
     *
     * @param id the user id
     * @throws UnauthorizedException if not logged in as that user or admin
     */
    void checkBasicAuthorization(Long id);

    /**
     * Checks if the client is logged in as the owner of the given script,
     * or is an admin.
     *
     * @param scriptId id of the script
     */
    boolean isOwnerOfScript(Long scriptId);

    /**
     * Checks if the logged in User is the owner of the script or admin.
     *
     * @param scriptId the id of the script
     * @return if the logged in User is a participant of the script
     */
    boolean isParticipantOfScript(Long scriptId);

    /**
     * Checks if the logged in User is the owner of the script or admin, or has the given email.
     *
     * @param scriptId the id of the script
     * @param email    the email
     * @throws UnauthorizedException if authorization fails
     */
    void checkMemberAuthorization(Long scriptId, String email);

    /**
     * Checks if the logged in User is a member of the script or admin.
     *
     * @param scriptId the id of the script
     * @throws UnauthorizedException if authorization fails
     */
    void checkMemberAuthorization(Long scriptId);
}
