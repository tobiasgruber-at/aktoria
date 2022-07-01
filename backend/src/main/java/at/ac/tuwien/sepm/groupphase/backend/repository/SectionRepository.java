package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Section;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Persistence repository for section entities.
 *
 * @author Julia Bernold
 * @author Marvin Flandorfer
 */
@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {

    /**
     * Finds sections by owner.
     *
     * @param owner Owner of the sections
     * @return List of sections
     */
    List<Section> findByOwner(User owner);

    /**
     * Finds sections by owner and script.
     *
     * @param owner Owner of the sections.
     * @param scriptId Script id of the script for the sections
     * @return List of sections
     */
    @Query("select s "
        + "from Section s join User u on s.owner.id = u.id "
        + "join Script c on c.id = s.startLine.page.script.id "
        + "where u.id = :#{#owner.id} and c.id = :#{#scriptId}")
    List<Section> findByOwnerAndScriptId(@Param("owner") User owner,
                                         @Param("scriptId") Long scriptId);
}
