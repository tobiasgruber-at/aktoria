package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Script;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScriptRepository extends JpaRepository<Script, Long> {
    List<Script> getScriptByOwner(User owner);

    @Modifying
    @Query("SELECT s FROM Script s WHERE :user IN elements(s.participants)")
    List<Script> getScriptByParticipant(@Param("user") User user);

    @Query("select c "
        + "from Script c join Page p on p.script.id = c.id join Line l on l.page.id = p.id "
            + "join Session s on s.currentLine.id = l.id "
        + "where s.id = ?1")
    Script getScriptBySessionId(Long sessionId);
}
