package at.ac.tuwien.sepm.groupphase.backend.service.impl.parsing.line;

import at.ac.tuwien.sepm.groupphase.backend.service.parsing.line.Line;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.line.impl.LineImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Class for testing operations with lines.
 *
 * @author Simon Josef Kreuzpointner
 */

@ActiveProfiles("test")
@SpringBootTest
class LineImplUnitTest {

    private static Stream<ParameterizedTupleGetRoles> parameterizedTupleGetRolesProvider() {
        final List<ParameterizedTupleGetRoles> temp = new LinkedList<>();
        temp.add(new ParameterizedTupleGetRoles(List.of(new String[] {"ROLE"}), "ROLE This is my text."));
        temp.add(new ParameterizedTupleGetRoles(List.of(new String[] {"NAME SURNAME"}), "NAME SURNAME This is my text."));
        temp.add(new ParameterizedTupleGetRoles(List.of(new String[] {"ROLEA", "ROLEB"}), "ROLEA UND ROLEB This is my text."));
        temp.add(new ParameterizedTupleGetRoles(List.of(new String[] {"ROLEA", "ROLEB"}), "ROLEA / ROLEB This is my text."));
        temp.add(new ParameterizedTupleGetRoles(List.of(new String[] {"ROLEA", "ROLEB"}), "ROLEA/ROLEB This is my text."));
        temp.add(new ParameterizedTupleGetRoles(List.of(new String[] {"MR. NAME"}), "MR. NAME This is my text."));
        temp.add(new ParameterizedTupleGetRoles(List.of(new String[] {"DR. MR. NAME"}), "DR. MR. NAME This is my text."));
        temp.add(new ParameterizedTupleGetRoles(List.of(new String[] {"NAME-SURNAME"}), "NAME-SURNAME This is my text."));
        temp.add(new ParameterizedTupleGetRoles(List.of(new String[] {"PETER P."}), "PETER P. This is my text."));
        temp.add(new ParameterizedTupleGetRoles(List.of(new String[] {"MS. NAME-SURNAME"}), "MS. NAME-SURNAME This is my text."));

        return temp.stream();
    }

    private static Stream<ParameterizedTupleGetContent> parameterizedTupleGetContentProvider() {
        final List<ParameterizedTupleGetContent> temp = new LinkedList<>();
        temp.add(new ParameterizedTupleGetContent("This is my text.", "ROLE This is my text."));
        temp.add(new ParameterizedTupleGetContent("This is my text", "ROLE This is my text"));
        temp.add(new ParameterizedTupleGetContent("This is my text.", "ROLE This is my text. "));
        temp.add(new ParameterizedTupleGetContent("This is my text.", "ROLE       This is my text."));
        temp.add(new ParameterizedTupleGetContent("This is my text.", "ROLE This is       my text."));
        temp.add(new ParameterizedTupleGetContent("This is my text.", "123 This is my text."));
        temp.add(new ParameterizedTupleGetContent("This is my text.", " 123 This is my text."));
        temp.add(new ParameterizedTupleGetContent("This is my text.", "123 This is my text. "));
        temp.add(new ParameterizedTupleGetContent("This is my text.", "123 This is     my text."));
        temp.add(new ParameterizedTupleGetContent("", "123"));
        temp.add(new ParameterizedTupleGetContent("123.", "123."));

        return temp.stream();
    }

    private static Stream<ParameterizedTupleGetRaw> parameterizedTupleGetRawProvider() {
        final List<ParameterizedTupleGetRaw> temp = new LinkedList<>();
        temp.add(new ParameterizedTupleGetRaw("ROLE This is my text.", "ROLE This is my text."));
        temp.add(new ParameterizedTupleGetRaw("ROLE This is my text.", "ROLE       This is my text."));
        temp.add(new ParameterizedTupleGetRaw("ROLE This is my text.", "ROLE This is my text. "));
        temp.add(new ParameterizedTupleGetRaw("ROLE This is my text.", " ROLE This is my text."));
        temp.add(new ParameterizedTupleGetRaw("ROLE This is my text.", "ROLE This is my \ntext."));
        temp.add(new ParameterizedTupleGetRaw("ROLE This is my text.", "ROLE This is my text.\n"));
        temp.add(new ParameterizedTupleGetRaw("ROLE This is my text.", "ROLE\t\tThis is my text."));
        temp.add(new ParameterizedTupleGetRaw("", "\n"));
        temp.add(new ParameterizedTupleGetRaw("\f", "\f"));

        return temp.stream();
    }

    @ParameterizedTest
    @DisplayName("getRoles() returns the correct roles")
    @MethodSource("parameterizedTupleGetRolesProvider")
    void getRoles(ParameterizedTupleGetRoles input) {
        final LineImpl l = new LineImpl(input.input, 0L);
        assertEquals(input.expected, l.getRoles());
    }

    @ParameterizedTest
    @DisplayName("getContent() returns the correct content")
    @MethodSource("parameterizedTupleGetContentProvider")
    void getContent(ParameterizedTupleGetContent input) {
        final LineImpl l = new LineImpl(input.input, 0L);
        assertEquals(input.expected, l.getContent());
    }

    @ParameterizedTest
    @DisplayName("getRaw() returns the correct value")
    @MethodSource("parameterizedTupleGetRawProvider")
    void getRaw(ParameterizedTupleGetRaw input) {
        final LineImpl l = new LineImpl(input.input, 0L);
        assertEquals(input.expected, l.getRaw());
    }

    private record ParameterizedTupleGetRoles(List<String> expected, String input) {
    }

    private record ParameterizedTupleGetContent(String expected, String input) {
    }

    private record ParameterizedTupleGetRaw(String expected, String input) {
    }

    @Nested
    @DisplayName("getConflictType()")
    class GetConflictType {
        @ParameterizedTest
        @DisplayName("returns assignment required")
        @ValueSource(strings = {
            "ALLE This is my text."
        })
        void getConflictTypeAssignmentRequired(String input) {
            final LineImpl l = new LineImpl(input, 0L);
            assertEquals(Line.ConflictType.ASSIGNMENT_REQUIRED, l.getConflictType());
        }

        @ParameterizedTest
        @DisplayName("returns null")
        @ValueSource(strings = {
            "ROLE This is my text.",
            "NAME SURNAME This is my text.",
            "ROLEA UND ROLEB This is my text.",
            "ROLEA / ROLEB This is my text.",
            "ROLEA/ROLEB This is my text.",
            "MR. NAME This is my text.",
            "DR. MR. NAME This is my text.",
            "NAME-SURNAME This is my text.",
            "PETER P. This is my text."
        })
        void getConflictTypeNull(String input) {
            final LineImpl l = new LineImpl(input, 0L);
            assertNull(l.getConflictType());
        }
    }

    @Nested
    @DisplayName("hasRoles()")
    class HasRoles {
        @ParameterizedTest
        @DisplayName("returns true")
        @ValueSource(strings = {
            "ROLE This is my text.",
            "NAME SURNAME This is my text.",
            "ROLEA UND ROLEB This is my text.",
            "ROLEA / ROLEB This is my text.",
            "ROLEA/ROLEB This is my text.",
            "MR. NAME This is my text.",
            "DR. MR. NAME This is my text.",
            "NAME-SURNAME This is my text.",
            "PETER P. This is my text."
        })
        void hasRolesTrue(String value) {
            final LineImpl l = new LineImpl(value, 0L);
            assertTrue(l.hasRoles());
        }

        @ParameterizedTest
        @DisplayName("returns false")
        @ValueSource(strings = {
            "This is my text.",
            "(This is an instruction.)",
            " ",
            "",
            "\n",
            "\f"
        })
        void hasRolesFalse(String value) {
            final LineImpl l = new LineImpl(value, 0L);
            assertFalse(l.hasRoles());
        }
    }

    @Nested
    @DisplayName("isCompletedLine()")
    class IsCompletedLine {
        @ParameterizedTest
        @DisplayName("returns true")
        @ValueSource(strings = {
            "This is a completed line.",
            "This is a completed line!",
            "This is a completed line?",
            "This is a completed line…",
            "This is a completed line/",
            "This is a completed line”",
            "This is a completed line\"",
            "This is a completed line)",
            "Erster Akt",
            "Zweiter Akt",
            "Zwölfter Akt",
            "Vorhang",
            "Ende"
        })
        void isCompletedLineTrue(String value) {
            final LineImpl l = new LineImpl(value, 0L);
            assertTrue(l.isCompletedLine());
        }

        @ParameterizedTest
        @DisplayName("returns false")
        @ValueSource(strings = {
            "This is not a completed line",
            "This is not a completed line ",
            "This is not a completed line -",
            " ",
            "",
            "\n",
            "\t",
            "\f"
        })
        void isCompletedLineFalse(String value) {
            final LineImpl l = new LineImpl(value, 0L);
            assertFalse(l.isCompletedLine());
        }
    }
}