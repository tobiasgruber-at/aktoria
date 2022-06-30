package at.ac.tuwien.sepm.groupphase.backend.service.parsing.scriptparser.impl;

import at.ac.tuwien.sepm.groupphase.backend.service.parsing.line.Line;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.line.impl.LineImpl;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.script.ParsedScript;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.scriptparser.ScriptParser;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * An implementation of ScriptParser.
 *
 * @author Simon Josef Kreuzpointner
 */
@Slf4j
public class ScriptParserImpl implements ScriptParser {
    private final List<String> allRoles = new LinkedList<>();
    private String raw;
    private Long curPageIndex;

    public ScriptParserImpl(String raw) {
        this.raw = raw;
        this.curPageIndex = 0L;
    }

    /**
     * Processes all staged lines.
     * <br>
     * Processes all previously extracted lines, aggregates found roles, and merges
     * adjacent lines, that do not end correctly.
     * <br>
     * For example, having two
     * lines with the content being "This is" and "my line." it is pretty clear,
     * that this line was wrongly split into two. Since the first line is not properly
     * endet, these will be merged into a new line.
     *
     * @param line        the line to be processed with all other currently staged lines
     * @param stagedLines all currently staged lines
     */
    private void processLine(Line line, Stack<Line> stagedLines) {
        log.trace("processLine(line = {}, stagedLines = {})", line, stagedLines);

        if (line.hasRoles()) {
            for (String c : line.getRoles()) {
                if (!allRoles.contains(c)) {
                    allRoles.add(c);
                }
            }

            stagedLines.add(line);
        } else {
            if (stagedLines.empty()) {
                stagedLines.add(line);
            } else {
                Line previousLine = stagedLines.peek();

                if (previousLine.isCompletedLine()) {
                    stagedLines.add(line);
                } else {
                    stagedLines.pop();
                    stagedLines.add(LineImpl.join(previousLine, line));
                }
            }
        }
    }

    /**
     * Handles split up roles.
     * <br>
     * Handles all lines where roles where split up in the parsing process. This can happen
     * when the role definition is long enough to get wrapped into more lines.
     *
     * @param stagedLines all staged lines
     * @return a list of all handled lines
     */
    private List<Line> handleSplitRoles(List<Line> stagedLines) {
        log.trace("handleSplitRoles(stagedLines = {})", stagedLines);

        List<Line> handledLines = new LinkedList<>();

        for (int i = 1; i < stagedLines.size(); i++) {
            Line previousLine = stagedLines.get(i - 1);
            Line curLine = stagedLines.get(i);

            if (!(previousLine.hasRoles() && curLine.hasRoles())) {
                handledLines.add(previousLine);
                if (i == stagedLines.size() - 1) {
                    handledLines.add(curLine);
                }
                continue;
            }

            List<String> previousCharacters = previousLine.getRoles();
            List<String> curCharacters = curLine.getRoles();

            if (previousCharacters.size() > 1 || curCharacters.size() > 1) {
                handledLines.add(previousLine);
                if (i == stagedLines.size() - 1) {
                    handledLines.add(curLine);
                }
                continue;
            }

            List<String> combinations = new LinkedList<>();
            combinations.add(String.join("", previousCharacters.get(0), curCharacters.get(0)));
            combinations.add(String.join(" ", previousCharacters.get(0), curCharacters.get(0)));

            boolean handled = false;

            for (String combination : combinations) {
                if (allRoles.contains(combination)) {
                    String newRaw = String.join(" ", combination, previousLine.getContent(), curLine.getContent());
                    handledLines.add(new LineImpl(newRaw, previousLine.getPage()));

                    allRoles.remove(curCharacters.get(0));

                    handled = true;
                    i++;
                    break;
                }
            }

            if (!handled) {
                handledLines.add(previousLine);

                if (i == stagedLines.size() - 1) {
                    handledLines.add(curLine);
                }
            }
        }

        return handledLines;
    }

    @Override
    public ParsedScript parse() {
        log.trace("parse()");

        Stack<Line> stagedLines = new Stack<>();

        raw = raw.trim();
        raw = raw.replace("\r\n", "\n");
        raw = raw.replace("\r", "\n");

        String[] splitContent = raw.split("\n\n");
        for (String rawLine : splitContent) {
            Line curLine = new LineImpl(rawLine, curPageIndex);

            if (curLine.isEmpty()) {
                continue;
            }

            if (curLine.getRaw().equals("\f")) {
                curPageIndex++;
                continue;
            }

            List<Line> possibleInternalLines = curLine.getPossibleInternalLines();
            for (Line p : possibleInternalLines) {
                processLine(p, stagedLines);
            }
        }

        final List<Line> handledLines = handleSplitRoles(stagedLines);
        return new ParsedScript(handledLines, allRoles);
    }
}
