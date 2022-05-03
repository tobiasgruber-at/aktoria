package at.ac.tuwien.sepm.groupphase.backend.service.parsing.script;

import at.ac.tuwien.sepm.groupphase.backend.service.parsing.phrase.Phrase;

import java.util.List;
import java.util.StringJoiner;

public class ParsedScript {
    List<Phrase> phrases;
    List<String> characters;

    public ParsedScript(List<Phrase> phrases, List<String> characters) {
        this.phrases = phrases;
        this.characters = characters;
    }

    public List<Phrase> getPhrases() {
        return phrases;
    }

    public List<String> getCharacters() {
        return characters;
    }

    @Override
    public String toString() {
        StringJoiner stringJoiner = new StringJoiner("\n");

        for (Phrase p : phrases) {
            stringJoiner.add(p.toString());
        }

        return stringJoiner.toString();
    }
}
