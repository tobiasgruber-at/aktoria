package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.SecureToken;
import at.ac.tuwien.sepm.groupphase.backend.enums.TokenType;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;

/**
 * Service for the secure Token.
 *
 * @author Nikolaus Peter
 */
public interface SecureTokenService {
    /**
     * Creates a new secure token.
     *
     * @param type type of the secure token (reset_password or verify_email)
     * @return the created secure Token
     */
    SecureToken createSecureToken(TokenType type);

    /**
     * Saves the secure token in the database and deletes all expired tokens.
     *
     * @param token SecureToken that will be saved
     */
    void saveSecureToken(SecureToken token);

    /**
     * Finds the SecureToken that belongs to the token string.
     *
     * @param token the token string
     * @return the corresponding SecureToken
     */
    SecureToken findByToken(String token) throws NotFoundException;

    /**
     * Removes the Token from the database.
     *
     * @param token the token string
     */
    void removeToken(String token);
}
