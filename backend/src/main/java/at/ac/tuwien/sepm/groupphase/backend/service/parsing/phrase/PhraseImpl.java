package at.ac.tuwien.sepm.groupphase.backend.service.parsing.phrase;

import java.util.List;

public class PhraseImpl implements Phrase {
    @Override
    public int getPage() {
        return 0;
    }

    @Override
    public void setPage(int value) {

    }

    @Override
    public ConflictType getConflictType() {
        return null;
    }

    @Override
    public void setConflictType(ConflictType value) {

    }

    @Override
    public String getRaw() {
        return null;
    }

    @Override
    public List<String> getCharacters() {
        return null;
    }

    @Override
    public String getContent() {
        return null;
    }

    @Override
    public List<Phrase> getPossibleInternalPhrases() {
        return null;
    }

    @Override
    public boolean isCompletedPhrase() {
        return false;
    }

    @Override
    public boolean hasCharacters() throws IllegalStateException {
        return false;
    }

    @Override
    public int length() {
        return 0;
    }

    @Override
    public char charAt(int index) {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return null;
    }
}
