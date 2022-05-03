package at.ac.tuwien.sepm.groupphase.backend.service.parsing.phrase;

import java.util.List;

/**
 * This interface describes a phrase in a theater play script. A phrase is a section of the script
 * that a specific role or specific roles say.
 * <p>
 * The content of the phrase does not contain any new lines.
 */
public interface Phrase extends CharSequence {

    /**
     * Gets the page of the phrase.
     *
     * @return the page
     */
    int getPage();

    /**
     * Sets the page of the phrase.
     *
     * @param value the page
     */
    void setPage(int value);

    /**
     * Gets the conflict type of this phrase.
     *
     * @return the conflict type
     */
    ConflictType getConflictType();

    /**
     * Sets the conflict type of this phrase.
     *
     * @param value the conflict type
     */
    void setConflictType(ConflictType value);

    /**
     * Gets the raw content of the phrase.
     *
     * @return the raw content of the phrase.
     */
    String getRaw();

    /**
     * Gets the characters of this phrase. If the phrase is not yet decompiled the return value
     * is undefined.
     *
     * @return the names of the characters of this phrase.
     */
    List<String> getCharacters();

    /**
     * Gets the content of the phrase. f the phrase is not yet decompiled the return value
     * is undefined.
     *
     * @return the content of the phrase.
     */
    String getContent();

    /**
     * This function looks for possible other phrases that are mistakenly in this
     * phrase. It returns a list of all phrases that where found. If no other phrases where found
     * the only item is this phrase.
     *
     * @return a list of found phrases in side of this phrase
     */
    List<Phrase> getPossibleInternalPhrases();

    /**
     * Returns true if the phrase is considered completed.
     *
     * @return true if the phrase is considered completed and false otherwise
     */
    boolean isCompletedPhrase();

    /**
     * Returns true if the phrase has characters.
     *
     * @return true if the phrase has characters and false otherwise
     * @throws IllegalStateException when the phrase was not yet decomposed
     */
    boolean hasCharacters() throws IllegalStateException;

    @Override
    boolean isEmpty();

    @Override
    String toString();

    enum ConflictType {VERIFICATION_REQUIRED, ASSIGNMENT_REQUIRED}
}

