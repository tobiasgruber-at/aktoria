package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Persistence repository for role entities.
 *
 * @author Marvin Flandorfer
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
