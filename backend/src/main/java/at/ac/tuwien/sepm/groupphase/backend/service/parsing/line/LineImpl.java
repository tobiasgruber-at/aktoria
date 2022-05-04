package at.ac.tuwien.sepm.groupphase.backend.service.parsing.line;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LineImpl implements Line {
    private static final String[] SENTENCE_DELIMITERS = { ".", "!", "?", "\"", "”", "/", ")", "…" };
    private static final String[] MULTI_ROLES_DELIMITERS = { " UND ", "/" };
    private Line.ConflictType conflictType;
    private boolean isDecomposed;
    private String raw;
    private List<String> characters;
    private String content;
    private int page;

    public LineImpl(String raw) {
        this.raw = raw;
        this.isDecomposed = false;
        this.conflictType = null;

        clean();
        removePageNumber();
        collapseWhitespaces();
        decomposePhrase();
    }

    public static Line join(Line a, Line b) {
        return new LineImpl(String.join(" ", a.getRaw(), b.getRaw()));
    }

    private void clean() {
        raw = raw.replace("\n", " ");
        collapseWhitespaces();
        raw = raw.replaceAll("\\t+", "");

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
        raw = raw.replaceAll(" +", " ");
    }

    private void decomposePhrase() {
        Pattern pattern = Pattern.compile("^[A-Z\\s\\.-]+(?=\\s)");
        Matcher matcher = pattern.matcher(raw);

        if (matcher.find()) {
            String charactersDeclaration = matcher.group().trim();

            // TODO: check if the content starts with an uppercase letter
            // if not, check if the character declaration snatched the starting letter
            // e.g. for phrases like "BOB   O nein!"

            characters = getCharactersFromDeclaration(charactersDeclaration);
            content = raw.substring(matcher.end()).trim();
        } else {
            characters = null;
            content = raw.trim();
        }

        isDecomposed = true;
    }

    private String compilePhrase() {
        if (hasRoles()) {
            String temp;
            if (characters.size() > 1) {
                temp = String.join("/", characters);
            } else {
                temp = characters.get(0);
            }

            return String.format("[%s] %s", temp, content);
        } else {
            return String.format("%s", content);
        }
    }

    private List<String> getCharactersFromDeclaration(String charactersDeclaration) {
        List<String> temp;

        String delimiter = getCharacterDelimiter(charactersDeclaration);
        if (delimiter != null) {
            temp = getMultipleCharacters(delimiter, charactersDeclaration);
        } else {
            temp = new LinkedList<>();
            temp.add(charactersDeclaration.trim().toUpperCase(Locale.GERMAN));
        }

        return temp;
    }

    private String getCharacterDelimiter(String charactersDeclaration) {
        for (String delimiter : MULTI_ROLES_DELIMITERS) {
            if (charactersDeclaration.contains(delimiter)) {
                return delimiter;
            }
        }

        return null;
    }

    private List<String> getMultipleCharacters(String delimiter, String charactersDeclaration) {
        String[] temp = charactersDeclaration.split(delimiter);
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
        return characters;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public List<Line> getPossibleInternalLines() {
        Pattern pattern = Pattern.compile("\\b(?<=\\.|\\s|^)[A-Z\\s\\.]+(?=\\s|\\.|$)\\b");
        Matcher matcher = pattern.matcher(raw);

        List<Line> newPhrases = new LinkedList<>();
        Stack<Line> newPhrasesCollapsed = new Stack<>();

        int offset = 0;
        while (matcher.find()) {
            String sub = raw.substring(offset, matcher.start());

            if (sub.isEmpty()) continue;

            LineImpl newPhrase = new LineImpl(sub);
            newPhrase.setConflictType(ConflictType.VERIFICATION_REQUIRED);
            newPhrases.add(newPhrase);

            offset += sub.length();
        }

        // add the rest
        String sub = raw.substring(offset);
        LineImpl newPhrase = new LineImpl(sub);
        newPhrases.add(newPhrase);

        newPhrasesCollapsed.push(newPhrases.get(0));

        for (int i = 1; i < newPhrases.size(); i++) {
            Line previousPhrase = newPhrasesCollapsed.peek();
            Line currentPhrase = newPhrases.get(i);

            if (!previousPhrase.isCompletedLine()) {
                newPhrasesCollapsed.pop();

                Line temp = LineImpl.join(previousPhrase, currentPhrase);
                temp.setConflictType(ConflictType.VERIFICATION_REQUIRED);
                newPhrasesCollapsed.push(temp);
            } else {
                newPhrasesCollapsed.push(currentPhrase);
            }
        }

        return newPhrasesCollapsed;
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
            if (characters == null) {
                return false;
            } else {
                return characters.size() > 0;
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
            return compilePhrase();
        } else {
            return raw;
        }
    }
}
