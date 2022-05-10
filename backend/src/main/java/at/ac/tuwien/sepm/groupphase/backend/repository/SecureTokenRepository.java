package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.SecureToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;


public interface SecureTokenRepository extends JpaRepository<SecureToken, Long> {
    Optional<SecureToken> findByToken(String token);

    Long removeByToken(String token);

    @Modifying
    @Query("DELETE FROM SecureToken t where t.expireAt <= ?1")
    void deleteAllExpired(LocalDateTime now);
}
