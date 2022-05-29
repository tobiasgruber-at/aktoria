package at.ac.tuwien.sepm.groupphase.backend.persistence;

import at.ac.tuwien.sepm.groupphase.backend.entity.Line;
import at.ac.tuwien.sepm.groupphase.backend.entity.Page;
import at.ac.tuwien.sepm.groupphase.backend.entity.Script;
import at.ac.tuwien.sepm.groupphase.backend.repository.LineRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ScriptRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Disabled
@ActiveProfiles({ "test", "datagen" })
public class LineRepositoryUnitTest {

    @Autowired
    private LineRepository lineRepository;
    @Autowired
    private ScriptRepository scriptRepository;

    @Test
    @DirtiesContext
    @DisplayName("find all lines in a script")
    public void findByScriptIdCorrectly() {
        Script script = scriptRepository.getById(1L);
        List<Page> pages = script.getPages();
        Set<Line> expected = new HashSet<>();
        for (Page page : pages) {
            expected.addAll(page.getLines());
        }
        Set<Line> actual = new HashSet<>(lineRepository.findByScriptId(script.getId()));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DirtiesContext
    @DisplayName("find all lines between two other lines in the same script")
    public void findByStartLineAndEndLineCorrectly() {
        Script script = scriptRepository.getById(1L);
        List<Page> pages = script.getPages();
        Set<Line> expected = new HashSet<>();
        for (Page page : pages) {
            if (page.getIndex() > 1 && page.getIndex() < 7) {
                expected.addAll(page.getLines());
            }
        }
        Line startLine = lineRepository.getById(11L);
        Line endLine = lineRepository.getById(60L);
        Set<Line> actual = new HashSet<>(lineRepository.findByStartLineAndEndLine(startLine, endLine));
        assertThat(actual).isEqualTo(expected);
    }
}
