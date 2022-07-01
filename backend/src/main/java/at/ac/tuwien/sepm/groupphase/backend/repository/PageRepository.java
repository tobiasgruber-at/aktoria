package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Persistence repository for page entities.
 *
 * @author Marvin Flandorfer
 */
@Repository
public interface PageRepository extends JpaRepository<Page, Long> {
}
