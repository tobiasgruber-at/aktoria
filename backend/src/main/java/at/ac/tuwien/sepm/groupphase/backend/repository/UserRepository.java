package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Persistence repository for user entities.
 *
 * @author Marvin Flandorfer
 * @author Luke Nemeskeri
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by email.
     *
     * @param email Email of the user
     * @return Optional user (may be empty)
     */
    Optional<User> findByEmail(String email);
}
