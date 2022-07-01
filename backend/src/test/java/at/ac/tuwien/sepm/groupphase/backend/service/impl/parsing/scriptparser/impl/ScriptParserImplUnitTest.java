package at.ac.tuwien.sepm.groupphase.backend.service.impl.parsing.scriptparser.impl;

import at.ac.tuwien.sepm.groupphase.backend.service.parsing.line.Line;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.line.impl.LineImpl;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.script.ParsedScript;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.scriptparser.impl.ScriptParserImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Class for testing script-parsing.
 *
 * @author Simon Josef Kreuzpointner
 */

@ActiveProfiles("test")
@SpringBootTest
class ScriptParserImplUnitTest {

    @Test
    @DirtiesContext
    @DisplayName("parse() parses the script correctly")
    void parse() throws IOException {
        final String input = Files.readString(Path.of("./src/test/resources/service/parsing/scriptParser/parse_input.txt"));
        final List<String> rawLines = Files.readAllLines(Path.of("./src/test/resources/service/parsing/scriptParser/parse_expected_lines.txt"));
        final List<String> roles = Files.readAllLines(Path.of("./src/test/resources/service/parsing/scriptParser/parse_expected_roles.txt"));

        final List<Line> lines = rawLines.stream().map(line -> (Line) new LineImpl(line, 0L)).toList();

        Long pageIndex = 0L;
        Long lineIndex = 0L;
        for (Line l : lines) {
            l.setPage(pageIndex);
            if (lineIndex == 5) {
                pageIndex++;
            }
            lineIndex++;
        }

        final ScriptParserImpl parser = new ScriptParserImpl(input);
        final ParsedScript expectedParsedScript = new ParsedScript(lines, roles);
        final ParsedScript actualParsedScript = parser.parse();

        assertEquals(expectedParsedScript, actualParsedScript);
    }
}