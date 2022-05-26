package at.ac.tuwien.sepm.groupphase.backend.validation.impl;

import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.line.impl.LineImpl;
import at.ac.tuwien.sepm.groupphase.backend.validation.LineValidation;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A specific implementation of LineValidation.
 *
 * @author Simon Josef Kreuzpointner
 */
@Component
public class LineValidationImpl implements LineValidation {

    @Override
    public void validateContentInput(String content) {
        if (content == null) {
            throw new ValidationException("Zeileninhalt muss angegeben werden!");
        }
        if (content.length() <= 0) {
            throw new ValidationException("Zeileninhalt ist zu kurz.");
        }
        if (content.length() > 5000) {
            throw new ValidationException("Zeileninhalt ist zu lang.");
        }
        if (!content.equals(content.trim())) {
            throw new ValidationException("Zeileninhalt darf nicht mit Leerzeichen beginnen oder enden.");
        }
        if (Arrays.stream(new String[] { "\r", "\n", "\f", "\t" }).anyMatch(content::contains)) {
            throw new ValidationException("Zeileninhalt beinhaltet nicht valide Leerzeichen!");
        }
        boolean isSpecialSentence = false;
        for (String p : LineImpl.SPECIAL_SENTENCES_PATTERNS) {
            Pattern pattern = Pattern.compile(p);
            Matcher matcher = pattern.matcher(content);
            if (matcher.matches()) {
                isSpecialSentence = true;
                break;
            }
        }
        if (!isSpecialSentence) {
            char firstChar = content.charAt(0);
            if (Character.isLowerCase(firstChar)) {
                throw new ValidationException("Zeileninhalt darf nicht mit einem Kleinbuchstaben beginnen.");
            } else if (!Character.isUpperCase(firstChar)) {
                boolean hasCorrectStart = false;
                for (String s : LineImpl.SPECIAL_SENTENCE_STARTERS) {
                    if (s.equals(Character.toString(firstChar))) {
                        hasCorrectStart = true;
                        break;
                    }
                }
                if (!hasCorrectStart) {
                    throw new ValidationException("Zeileninhalt beginnt nicht korrekt.");
                }
            }
            if (Character.isLowerCase(content.charAt(0))) {
                throw new ValidationException("Zeileninhalt muss mit einem Großbuchstaben beginnen.");
            }

            boolean isCompleted = false;
            for (String delimiter : LineImpl.SENTENCE_DELIMITERS) {
                final String lastChar = content.substring(content.length() - 1);
                if (lastChar.equals(delimiter)) {
                    isCompleted = true;
                    break;
                }
            }
            if (!isCompleted) {
                throw new ValidationException("Zeileninhalt muss korrekt beendet werden.");
            }
        }
    }
}
