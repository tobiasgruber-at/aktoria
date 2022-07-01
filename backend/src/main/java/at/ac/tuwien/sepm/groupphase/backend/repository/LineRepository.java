package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Line;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Persistence repository for line entities.
 *
 * @author Marvin Flandorfer
 */
@Repository
public interface LineRepository extends JpaRepository<Line, Long> {

    /**
     * Finds all lines belonging to a certain script.
     *
     * @param scriptId id of the script
     * @return List of lines
     */
    @Query("select l "
        + "from Line l join Page p on l.page.id = p.id join Script s on p.script.id = s.id "
        + "where s.id = ?1 "
        + "order by p.id asc, l.id asc")
    List<Line> findByScriptId(Long scriptId);

    /**
     * Finds all lines between a start and an end line within the same script.
     *
     * @param startLine Start line
     * @param endLine End line
     * @return List of lines
     */
    @Query("select l "
        + "from Line l join Page p on l.page.id = p.id join Script s on p.script.id = s.id "
        + "where s.id = :#{#startLine.page.script.id} "
        + "and p.index between :#{#startLine.page.index} and :#{#endLine.page.index} "
        + "and l.index >= case "
        + "when p.index = :#{#startLine.page.index} then :#{#startLine.index} "
        + "else 0 end "
        + "and l.index <= case "
        + "when p.index = :#{#endLine.page.index} then :#{#endLine.index} "
        + "else (select max (l.index) from l) end "
        + "order by p.index asc, l.index asc")
    List<Line> findByStartLineAndEndLine(@Param("startLine") Line startLine,
                                         @Param("endLine") Line endLine);
}
