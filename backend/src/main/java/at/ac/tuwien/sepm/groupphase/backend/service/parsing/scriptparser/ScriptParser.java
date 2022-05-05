package at.ac.tuwien.sepm.groupphase.backend.service.parsing.scriptparser;

import at.ac.tuwien.sepm.groupphase.backend.service.parsing.script.ParsedScript;

/**
 * @author Simon Josef Kreuzpointner
 *
 * Describes a theater play script parser.
 */
public interface ScriptParser {
    /**
     * Parses the script.
     *
     * <p>Parses the script into pages, roles and lines. For a correct result
     * the given input has to be conformed to a given format standard.
     *
     * <p>Role declarations have to be written in uppercase. The line that is spoken
     * by the given role has to follow the role declaration.
     *
     * <p>Lines that are not leaded by a role declaration are given no role.
     *
     * <p>A page contains all lines, that start on this page.
     *
     * @return a new instance of ParsedScript
     */
    ParsedScript parse();
}
