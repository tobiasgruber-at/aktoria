package at.ac.tuwien.sepm.groupphase.backend.service.parsing.script;

import at.ac.tuwien.sepm.groupphase.backend.service.parsing.line.Line;

import java.util.List;
import java.util.StringJoiner;

public class ParsedScript {
    List<Line> lines;
    List<String> characters;

    public ParsedScript(List<Line> lines, List<String> characters) {
        this.lines = lines;
        this.characters = characters;
    }

    public List<Line> getPhrases() {
        return lines;
    }

    public List<String> getCharacters() {
        return characters;
    }

    @Override
    public String toString() {
        StringJoiner stringJoiner = new StringJoiner("\n");

        for (Line p : lines) {
            stringJoiner.add(p.toString());
        }

        return stringJoiner.toString();
    }
}
