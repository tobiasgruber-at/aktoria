package at.ac.tuwien.sepm.groupphase.backend.service.parsing.line;

import java.util.List;

/**
 * Describes a line in a theater play script.
 * <br>
 * A line is a section of the script
 * that a specific role or multiple specific roles say.
 * <br>
 * The content of the line does not contain any new lines.
 *
 * @author Simon Josef Kreuzpointner
 */
public interface Line extends CharSequence {

    /**
     * Gets the page of the current line.
     *
     * @return the page
     */
    int getPage();

    /**
     * Sets the page of the line.
     *
     * @param value the page
     */
    void setPage(int value);

    /**
     * Gets the conflict type of this line.
     *
     * @return the conflict type
     */
    ConflictType getConflictType();

    /**
     * Sets the conflict type of this line.
     *
     * @param value the conflict type
     */
    void setConflictType(ConflictType value);

    /**
     * Gets the raw content of the line.
     *
     * @return the raw content of the line.
     */
    String getRaw();

    /**
     * Gets the roles of this line.
     * <br>
     * Note, if the line is not yet decompiled the return value
     * is undefined.
     *
     * @return the names of the roles of this line.
     */
    List<String> getRoles();

    /**
     * Gets the content of the line.
     * <br>
     * Note, if the line is not yet decompiled the return value
     * is undefined.
     *
     * @return the content of the line.
     */
    String getContent();

    /**
     * Looks for possible other lines that are mistakenly in this line.
     * <br>
     * It returns a list of all lines that where found. If no other lines where found
     * the only item contained is this line.
     *
     * @return a list of found lines in side of this line
     */
    List<Line> getPossibleInternalLines();

    /**
     * Returns true if the line is considered completed.
     * <br>
     * A line is considered deleted if one of the following characters are found
     * as the last char:
     * <ul>
     *     <li>punctuations (".", "!", "?")
     *     <li>ellipsis ("…")
     *     <li>slash ("/")
     *     <li>quotation marks (“””, “"”)
     *     <li>closing round bracket (")")
     * </ul>
     * Or if the line is a special case:
     * <ul>
     *     <li>Act declarations (* Akt)
     *     <li>Curtain declarations (Vorhang)
     *     <li>Ending declarations (Ende)
     * </ul>
     *
     * @return true if the line is considered completed and false otherwise
     */
    boolean isCompletedLine();

    /**
     * Returns true if the line has roles.
     *
     * @return true if the line has roles and false otherwise
     * @throws IllegalStateException when the line was not yet decomposed
     */
    boolean hasRoles() throws IllegalStateException;

    /**
     * Returns true if this line is considered empty.
     * <br>
     * This line is considered empty if the raw content is empty.
     *
     * @return true if the line is empty and false otherwise
     */
    @Override
    boolean isEmpty();

    /**
     * Returns a stringed representation of this line.
     *
     * @return a stringed representation of this line.
     */
    @Override
    String toString();

    /**
     * The type of possible conflict for a line.
     */
    enum ConflictType {
        VERIFICATION_REQUIRED, ASSIGNMENT_REQUIRED
    }
}

