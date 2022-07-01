package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.SecureToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Persistence repository for secure token entities.
 *
 * @author Nikolaus Peter
 * @author Simon Kreuzpointner
 */
public interface SecureTokenRepository extends JpaRepository<SecureToken, Long> {

    /**
     * Find secure token by string token.
     * @param token String of the token
     * @return Optional secure token (may be empty)
     */
    Optional<SecureToken> findByToken(String token);

    /**
     * Deletes secure token by string token.
     *
     * @param token String of the token
     * @return Id of the deleted token
     */
    Long deleteByToken(String token);

    /**
     * Deletes all expired secure tokens.
     *
     * @param now Local date time of the current time
     */
    @Modifying
    @Query("DELETE FROM SecureToken t where t.expireAt <= ?1")
    void deleteAllExpired(LocalDateTime now);
}
