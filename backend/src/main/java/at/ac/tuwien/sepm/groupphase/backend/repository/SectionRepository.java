package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Section;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
    List<Section> findByOwner(User owner);

    @Query("select s "
        + "from Section s join User u on s.owner.id = u.id "
            + "join Script c on c.id = s.startLine.page.script.id "
        + "where u.id = :#{#owner.id} and c.id = :#{#scriptId}")
    List<Section> findByOwnerAndScriptId(@Param("owner") User owner,
                                         @Param("scriptId") Long scriptId);
}
