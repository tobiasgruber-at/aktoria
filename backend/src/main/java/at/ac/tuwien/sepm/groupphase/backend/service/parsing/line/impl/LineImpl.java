package at.ac.tuwien.sepm.groupphase.backend.service.parsing.line.impl;

import at.ac.tuwien.sepm.groupphase.backend.service.parsing.line.Line;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An implementation of Line.
 *
 * @author Simon Josef Kreuzpointner
 */
@Slf4j
public class LineImpl implements Line {
    public static final String[] SENTENCE_DELIMITERS = { ".", "!", "?", "\"", "”", "/", ")", "…" };
    public static final String[] SPECIAL_SENTENCE_STARTERS = { "(", "\"", "(", "„" };
    public static final String[] SPECIAL_SENTENCES_PATTERNS = { "^.* Akt$", "^Vorhang$", "^Ende$" };
    private static final String[] MULTI_ROLES_DELIMITERS = { " UND ", "/", " / " };
    private static final String[] ALL_ROLES_IDENTIFIERS = { "ALLE", "BEIDE", "GEMEINSAM" };
    private Line.ConflictType conflictType;
    private boolean isDecomposed;
    private String raw;
    private List<String> roles;
    private String content;
    private Long page;
    private Long index;

    public LineImpl(String raw, Long page) {
        this.raw = raw;
        this.page = page;
        this.isDecomposed = false;
        this.conflictType = null;

        clean();
        removePageNumber();
        collapseWhitespaces();
        decomposeLine();
    }

    /**
     * Joins two existing lines.
     * <br>
     * This function generates a new line that consists of the joined raw content
     * of the given lines. The page index of the first line will be kept.
     *
     * @param a the first line
     * @param b the second line
     * @return a new line object that combines the two given lines
     */
    public static Line join(Line a, Line b) {
        log.trace("join(a = {}, b = {})", a, b);

        return new LineImpl(String.join(" ", a.getRaw(), b.getRaw()), a.getPage());
    }

    private void clean() {
        log.trace("clean()");

        raw = raw.replace("\n", " ");
        collapseWhitespaces();

        if (!raw.equals("\f")) {
            raw = raw.trim();
        }
    }

    private void removePageNumber() {
        log.trace("removePageNumber()");

        Pattern pattern = Pattern.compile("^\\d+\\s");
        Matcher matcher = pattern.matcher(raw);

        if (matcher.find()) {
            raw = raw.substring(matcher.end());
        } else {
            pattern = Pattern.compile("^\\d+$");
            matcher = pattern.matcher(raw);
            if (matcher.find()) {
                raw = "";
            }
        }

        if (!raw.equals("\f")) {
            raw = raw.trim();
        }
    }

    private void collapseWhitespaces() {
        log.trace("collapseWhitespaces()");

        raw = raw.replaceAll("\\t+", " ");
        raw = raw.replaceAll(" +", " ");
    }

    private void decomposeLine() {
        log.trace("decomposeLine()");

        Pattern pattern = Pattern.compile("^[A-Z\\s\\.\\-/]+(?=\\s[A-ZÖÜÄ0-9\\\"„”(])");
        Matcher matcher = pattern.matcher(raw);

        if (matcher.find()) {
            String stagedRolesDeclaration = matcher.group().trim();
            String stagedContent = raw.substring(matcher.end()).trim();

            // check if the content starts with an uppercase letter
            // if not, check if the role declaration snatched the starting letter
            // e.g. for lines like "BOB   O nein!"

            if (checkForOversuppliedRole(stagedContent)) {
                String[] temp = stagedRolesDeclaration.split(" ");
                stagedContent = String.join(" ", temp[temp.length - 1], stagedContent).trim();
                temp[temp.length - 1] = "";
                stagedRolesDeclaration = String.join(" ", temp).trim();
            }

            roles = getRolesFromDeclaration(stagedRolesDeclaration);
            content = stagedContent;
        } else {
            roles = null;
            content = raw.equals("\f") ? raw : raw.trim();
        }

        isDecomposed = true;
    }

    private boolean checkForOversuppliedRole(String content) {
        log.trace("checkForOversuppliedRole()");

        content = content.trim();

        if (content.isEmpty()) {
            return false;
        }

        char firstChar = content.charAt(0);
        return Character.isLowerCase(firstChar);
    }

    private String compileLine() {
        log.trace("compileLine()");

        if (hasRoles()) {
            String temp;
            if (roles.size() > 1) {
                temp = String.join("/", roles);
            } else {
                temp = roles.get(0);
            }

            return String.format("[%s] %s", temp, content);
        } else {
            return String.format("%s", content);
        }
    }

    private List<String> getRolesFromDeclaration(String rolesDeclaration) {
        log.trace("getRolesFromDeclaration(rolesDeclaration = {})", rolesDeclaration);

        if (rolesDeclaration.isEmpty()) {
            return null;
        }

        List<String> temp;

        String delimiter = getRoleDelimiter(rolesDeclaration);
        if (delimiter != null) {
            temp = getMultipleRoles(delimiter, rolesDeclaration);
        } else {
            temp = new LinkedList<>();
            temp.add(rolesDeclaration.trim().toUpperCase(Locale.GERMAN));
        }

        for (String role : temp) {
            for (String allIdentifier : ALL_ROLES_IDENTIFIERS) {
                if (role.equals(allIdentifier)) {
                    conflictType = ConflictType.ASSIGNMENT_REQUIRED;
                    break;
                }
            }

            if (conflictType != null) {
                break;
            }
        }

        return temp;
    }

    private String getRoleDelimiter(String rolesDeclaration) {
        log.trace("getRoleDelimiter(rolesDeclaration = {})", rolesDeclaration);

        for (String delimiter : MULTI_ROLES_DELIMITERS) {
            if (rolesDeclaration.contains(delimiter)) {
                return delimiter;
            }
        }

        return null;
    }

    private List<String> getMultipleRoles(String delimiter, String rolesDeclaration) {
        log.trace("getMultipleRoles(delimiter = {}, rolesDeclaration = {})", delimiter, rolesDeclaration);

        String[] temp = rolesDeclaration.split(delimiter);
        return (Arrays.stream(temp).map(value -> value.trim().toUpperCase(Locale.GERMAN))).toList();
    }

    @Override
    public Long getPage() {
        return page;
    }

    @Override
    public void setPage(Long value) {
        page = value;
    }

    @Override
    public Long getIndex() {
        return index;
    }

    @Override
    public void setIndex(Long value) {
        index = value;
    }

    @Override
    public ConflictType getConflictType() {
        return conflictType;
    }

    @Override
    public void setConflictType(ConflictType value) {
        conflictType = value;
    }

    @Override
    public String getRaw() {
        return raw;
    }

    @Override
    public List<String> getRoles() {
        return roles;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public List<Line> getPossibleInternalLines() {
        log.trace("getPossibleInternalLines()");

        Pattern pattern = Pattern.compile("\\b(?<!\\s[A-ZÖÜÄa-zöüä]\\.)(?<=\\s|^)[A-ZÖÜÄ\\s\\.\\-]+(?=\\s|$)\\b");
        Matcher matcher = pattern.matcher(raw);

        List<Line> newLines = new LinkedList<>();
        Stack<Line> newLinesCollapsed = new Stack<>();

        int offset = 0;
        while (matcher.find()) {
            String sub = raw.substring(offset, matcher.start());

            if (sub.isEmpty()) {
                continue;
            }

            LineImpl newLine = new LineImpl(sub, page);
            newLine.setConflictType(ConflictType.VERIFICATION_REQUIRED);
            newLines.add(newLine);

            offset += sub.length();
        }

        // add the rest
        String sub = raw.substring(offset);
        LineImpl newLine = new LineImpl(sub, page);
        newLines.add(newLine);

        newLinesCollapsed.push(newLines.get(0));

        for (int i = 1; i < newLines.size(); i++) {
            Line previousLine = newLinesCollapsed.peek();
            Line currentLine = newLines.get(i);

            if (!previousLine.isCompletedLine()) {
                newLinesCollapsed.pop();

                Line temp = LineImpl.join(previousLine, currentLine);
                temp.setConflictType(ConflictType.VERIFICATION_REQUIRED);
                newLinesCollapsed.push(temp);
            } else {
                newLinesCollapsed.push(currentLine);
            }
        }

        return newLinesCollapsed;
    }

    @Override
    public boolean isCompletedLine() {
        log.trace("isCompletedLine()");

        if (raw.isEmpty()) {
            return false;
        }

        String lastChar = raw.substring(raw.length() - 1);

        for (String delimiter : SENTENCE_DELIMITERS) {
            if (lastChar.equals(delimiter)) {
                return true;
            }
        }

        for (String p : SPECIAL_SENTENCES_PATTERNS) {
            Pattern pattern = Pattern.compile(p, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(raw);

            if (matcher.matches()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean hasRoles() throws IllegalStateException {
        log.trace("hasRoles()");

        if (isDecomposed) {
            if (roles == null) {
                return false;
            } else {
                return roles.size() > 0;
            }
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    public int length() {
        log.trace("length()");

        return raw.length();
    }

    @Override
    public char charAt(int index) {
        log.trace("charAt(index = {})", index);

        return raw.charAt(index);
    }

    @Override
    public boolean isEmpty() {
        log.trace("isEmpty()");

        return raw.isEmpty();
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        log.trace("subSequence(start = {}, end = {})", start, end);

        return raw.subSequence(start, end);
    }

    @Override
    public String toString() {
        log.trace("toString()");

        if (isDecomposed) {
            return compileLine();
        } else {
            return raw;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LineImpl line = (LineImpl) o;
        return isDecomposed == line.isDecomposed && conflictType == line.conflictType && Objects.equals(raw, line.raw) && Objects.equals(roles, line.roles) && Objects.equals(content, line.content)
            && Objects.equals(page, line.page) && Objects.equals(index, line.index);
    }

    @Override
    public int hashCode() {
        return Objects.hash(conflictType, isDecomposed, raw, roles, content, page, index);
    }
}
