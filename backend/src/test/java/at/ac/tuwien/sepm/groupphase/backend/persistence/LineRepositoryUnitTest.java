package at.ac.tuwien.sepm.groupphase.backend.persistence;

import at.ac.tuwien.sepm.groupphase.backend.entity.Line;
import at.ac.tuwien.sepm.groupphase.backend.entity.Page;
import at.ac.tuwien.sepm.groupphase.backend.entity.Script;
import at.ac.tuwien.sepm.groupphase.backend.repository.LineRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ScriptRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles({ "test", "datagen" })
@DataJpaTest
public class LineRepositoryUnitTest {

    @Autowired
    private LineRepository lineRepository;
    @Autowired
    private ScriptRepository scriptRepository;

    @Test
    @DisplayName("find all lines in a script")
    public void findByScriptIdCorrectly() throws Exception {
        Optional<Script> script = scriptRepository.findById(1L);
        if (script.isPresent()) {
            List<Page> pages = script.get().getPages();
            Set<Line> expected = new HashSet<>();
            for (Page page : pages) {
                expected.addAll(page.getLines());
            }
            Set<Line> actual = new HashSet<>(lineRepository.findByScriptId(script.get().getId()));
            assertThat(actual).isEqualTo(expected);
        } else {
            throw new Exception();
        }
    }

    @Test
    @DisplayName("find all lines between two other lines in the same script")
    public void findByStartLineAndEndLineCorrectly() throws Exception {
        Optional<Script> script = scriptRepository.findById(1L);
        if (script.isPresent()) {
            List<Page> pages = script.get().getPages();
            Set<Line> expected = new HashSet<>();
            for (Page page : pages) {
                if (page.getIndex() > 1 && page.getIndex() < 7) {
                    expected.addAll(page.getLines());
                }
            }
            Optional<Line> startLine = lineRepository.findById(11L);
            Optional<Line> endLine = lineRepository.findById(60L);
            if (startLine.isPresent() && endLine.isPresent()) {
                Set<Line> actual = new HashSet<>(lineRepository.findByStartLineAndEndLine(startLine.get(), endLine.get()));
                assertThat(actual).isEqualTo(expected);
            } else {
                throw new Exception();
            }
        } else {
            throw new Exception();
        }
    }
}
