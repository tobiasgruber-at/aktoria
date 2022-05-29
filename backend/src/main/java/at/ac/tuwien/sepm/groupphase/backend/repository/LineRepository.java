package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Line;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LineRepository extends JpaRepository<Line, Long> {

    @Query("select l "
        + "from Line l join Page p on l.page.id = p.id join Script s on p.script.id = s.id "
        + "where s.id = ?1 "
        + "order by p.id asc, l.id asc")
    List<Line> findByScriptId(Long scriptId);

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
