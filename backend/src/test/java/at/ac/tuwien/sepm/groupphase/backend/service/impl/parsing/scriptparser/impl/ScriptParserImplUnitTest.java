package at.ac.tuwien.sepm.groupphase.backend.service.impl.parsing.scriptparser.impl;

import at.ac.tuwien.sepm.groupphase.backend.service.parsing.line.Line;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.line.impl.LineImpl;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.script.ParsedScript;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.scriptparser.impl.ScriptParserImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
class ScriptParserImplUnitTest {

    @Test
    @DisplayName("parse() parses the script correctly")
    void parse() throws IOException {
        String input = Files.readString(Path.of("./src/test/resources/service/parsing/scriptparser/parse_input.txt"));
        List<String> rawLines = Files.readAllLines(Path.of("./src/test/resources/service/parsing/scriptparser/parse_expected_lines.txt"));
        List<String> roles = Files.readAllLines(Path.of("./src/test/resources/service/parsing/scriptparser/parse_expected_roles.txt"));

        List<Line> lines = rawLines.stream().map(line -> (Line) new LineImpl(line, 0)).toList();

        int pageIndex = 0;
        for (Line l : lines) {
            l.setPage(pageIndex);
            if (l.getRaw().equals("\f")) {
                pageIndex++;
            }
        }

        ScriptParserImpl parser = new ScriptParserImpl(input);
        ParsedScript expectedParsedScript = new ParsedScript(lines, roles);
        ParsedScript actualParsedScript = parser.parse();

        assertEquals(expectedParsedScript, actualParsedScript);
    }
}