package at.ac.tuwien.sepm.groupphase.backend.service.parsing.line;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LineImpl implements Line {
    private static final String[] SENTENCE_DELIMITERS = { ".", "!", "?", "\"", "”", "/", ")", "…" };
    private static final String[] MULTI_ROLES_DELIMITERS = { " UND ", "/", " / " };
    private Line.ConflictType conflictType;
    private boolean isDecomposed;
    private String raw;
    private List<String> roles;
    private String content;
    private int page;

    public LineImpl(String raw) {
        this.raw = raw;
        this.isDecomposed = false;
        this.conflictType = null;

        clean();
        removePageNumber();
        collapseWhitespaces();
        decomposeLine();
    }

    public static Line join(Line a, Line b) {
        return new LineImpl(String.join(" ", a.getRaw(), b.getRaw()));
    }

    private void clean() {
        raw = raw.replace("\n", " ");
        collapseWhitespaces();

        if (!raw.equals("\f")) raw = raw.trim();
    }

    private void removePageNumber() {
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

        if (!raw.equals("\f")) raw = raw.trim();
    }

    private void collapseWhitespaces() {
        raw = raw.replaceAll("\\t+", " ");
        raw = raw.replaceAll(" +", " ");
    }

    private void decomposeLine() {
        Pattern pattern = Pattern.compile("^[A-Z\\s\\.\\-/]+(?=\\s)");
        Matcher matcher = pattern.matcher(raw);

        if (matcher.find()) {
            String rolesDeclaration = matcher.group().trim();

            // TODO: check if the content starts with an uppercase letter
            // if not, check if the role declaration snatched the starting letter
            // e.g. for lines like "BOB   O nein!"

            roles = getRolesFromDeclaration(rolesDeclaration);
            content = raw.substring(matcher.end()).trim();
        } else {
            roles = null;
            content = raw.trim();
        }

        isDecomposed = true;
    }

    private String compileLine() {
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
        List<String> temp;

        String delimiter = getRoleDelimiter(rolesDeclaration);
        if (delimiter != null) {
            temp = getMultipleRoles(delimiter, rolesDeclaration);
        } else {
            temp = new LinkedList<>();
            temp.add(rolesDeclaration.trim().toUpperCase(Locale.GERMAN));
        }

        return temp;
    }

    private String getRoleDelimiter(String rolesDeclaration) {
        for (String delimiter : MULTI_ROLES_DELIMITERS) {
            if (rolesDeclaration.contains(delimiter)) {
                return delimiter;
            }
        }

        return null;
    }

    private List<String> getMultipleRoles(String delimiter, String rolesDeclaration) {
        String[] temp = rolesDeclaration.split(delimiter);
        return (Arrays.stream(temp).map(value -> value.trim().toUpperCase(Locale.GERMAN))).toList();
    }

    @Override
    public int getPage() {
        return page;
    }

    @Override
    public void setPage(int value) {
        page = value;
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
        Pattern pattern = Pattern.compile("\\b(?<=\\.|\\s|^)[A-Z\\s\\.]+(?=\\s|\\.|$)\\b");
        Matcher matcher = pattern.matcher(raw);

        List<Line> newLines = new LinkedList<>();
        Stack<Line> newLinesCollapsed = new Stack<>();

        int offset = 0;
        while (matcher.find()) {
            String sub = raw.substring(offset, matcher.start());

            if (sub.isEmpty()) continue;

            LineImpl newLine = new LineImpl(sub);
            newLine.setConflictType(ConflictType.VERIFICATION_REQUIRED);
            newLines.add(newLine);

            offset += sub.length();
        }

        // add the rest
        String sub = raw.substring(offset);
        LineImpl newLine = new LineImpl(sub);
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

    /**
     * Has to be called after clean().
     *
     * @return true if the Line is considered completed
     */
    @Override
    public boolean isCompletedLine() {
        if (raw.isEmpty()) return false;

        String lastChar = raw.substring(raw.length() - 1);

        for (String delimiter : SENTENCE_DELIMITERS) {
            if (lastChar.equals(delimiter)) return true;
        }

        return false;
    }

    @Override
    public boolean hasRoles() throws IllegalStateException {
        if (isDecomposed) {
            if (roles == null) {
                return false;
            } else {
                return roles.size() > 0;
            }
        } else throw new IllegalStateException();
    }

    @Override
    public int length() {
        return raw.length();
    }

    @Override
    public char charAt(int index) {
        return raw.charAt(index);
    }

    @Override
    public boolean isEmpty() {
        return raw.isEmpty();
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return raw.subSequence(start, end);
    }

    @Override
    public String toString() {
        if (isDecomposed) {
            return compileLine();
        } else {
            return raw;
        }
    }
}
