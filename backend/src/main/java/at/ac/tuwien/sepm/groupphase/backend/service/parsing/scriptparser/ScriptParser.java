package at.ac.tuwien.sepm.groupphase.backend.service.parsing.scriptparser;

import at.ac.tuwien.sepm.groupphase.backend.service.parsing.script.ParsedScript;

/**
 * Describes a theater play script parser.
 */
public interface ScriptParser {
    /**
     * Parses the script.
     * TODO: add documentation
     *
     * @return a new instance of ParsedScript
     */
    ParsedScript parse();
}
