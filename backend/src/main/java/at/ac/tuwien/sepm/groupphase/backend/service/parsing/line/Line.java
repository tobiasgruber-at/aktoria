package at.ac.tuwien.sepm.groupphase.backend.service.parsing.line;

import java.util.List;

/**
 * Describes a line in a theater play script.
 * <p>
 * A line is a section of the script
 * that a specific role or multiple specific roles say.
 * <p>
 * The content of the line does not contain any new lines.
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
     * Gets the roles of this line. If the line is not yet decompiled the return value
     * is undefined.
     *
     * @return the names of the roles of this line.
     */
    List<String> getRoles();

    /**
     * Gets the content of the line. f the line is not yet decompiled the return value
     * is undefined.
     *
     * @return the content of the line.
     */
    String getContent();

    /**
     * Looks for possible other lines that are mistakenly in this
     * line.
     * <p>
     * It returns a list of all lines that where found. If no other lines where found
     * the only item is this line.
     *
     * @return a list of found lines in side of this line
     */
    List<Line> getPossibleInternalLines();

    /**
     * Returns true if the line is considered completed.
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

    @Override
    boolean isEmpty();

    @Override
    String toString();

    enum ConflictType {VERIFICATION_REQUIRED, ASSIGNMENT_REQUIRED}
}

