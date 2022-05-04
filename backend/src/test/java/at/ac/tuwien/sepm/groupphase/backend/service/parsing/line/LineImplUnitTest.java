package at.ac.tuwien.sepm.groupphase.backend.service.parsing.line;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.context.ActiveProfiles;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class LineImplUnitTest {

    private static Stream<ParameterizedTupleGetRoles> tupleProvider() {
        List<ParameterizedTupleGetRoles> temp = new LinkedList<>();
        temp.add(new ParameterizedTupleGetRoles(List.of(new String[]{ "ROLE" }), "ROLE This is my text."));
        temp.add(new ParameterizedTupleGetRoles(List.of(new String[]{ "NAME SURENAME" }), "NAME SURENAME This is my text."));
        temp.add(new ParameterizedTupleGetRoles(List.of(new String[]{ "ROLEA", "ROLEB" }), "ROLEA UND ROLEB This is my text."));
        temp.add(new ParameterizedTupleGetRoles(List.of(new String[]{ "ROLEA", "ROLEB" }), "ROLEA / ROLEB This is my text."));
        temp.add(new ParameterizedTupleGetRoles(List.of(new String[]{ "ROLEA", "ROLEB" }), "ROLEA/ROLEB This is my text."));
        temp.add(new ParameterizedTupleGetRoles(List.of(new String[]{ "MR. NAME" }), "MR. NAME This is my text."));
        temp.add(new ParameterizedTupleGetRoles(List.of(new String[]{ "DR. MR. NAME" }), "DR. MR. NAME This is my text."));
        temp.add(new ParameterizedTupleGetRoles(List.of(new String[]{ "NAME-SURENAME" }), "NAME-SURENAME This is my text."));
        temp.add(new ParameterizedTupleGetRoles(List.of(new String[]{ "PETER P." }), "PETER P. This is my text."));
        temp.add(new ParameterizedTupleGetRoles(List.of(new String[]{ "MS. NAME-SURENAME" }), "MS. NAME-SURENAME This is my text."));

        return temp.stream();
    }

    @ParameterizedTest
    @DisplayName("getRoles() returns the correct roles")
    @MethodSource("tupleProvider")
    void getRoles(ParameterizedTupleGetRoles input) {
        LineImpl l = new LineImpl(input.input);
        assertEquals(input.expected, l.getRoles());
    }

    private record ParameterizedTupleGetRoles(List<String> expected, String input) {
    }

    @Nested
    @DisplayName("hasRoles()")
    class HasRoles {
        @ParameterizedTest(name = "[{index}] value = {0}")
        @DisplayName("returns true")
        @ValueSource(strings = {
                "ROLE This is my text.",
                "NAME SURENAME This is my text.",
                "ROLEA UND ROLEB This is my text.",
                "ROLEA / ROLEB This is my text.",
                "ROLEA/ROLEB This is my text.",
                "MR. NAME This is my text.",
                "DR. MR. NAME This is my text.",
                "NAME-SURENAME This is my text.",
                "PETER P. This is my text."
        })
        void hasRolesTrue(String value) {
            LineImpl l = new LineImpl(value);
            assertTrue(l.hasRoles());
        }

        @ParameterizedTest(name = "[{index}] value = {0}")
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
            LineImpl l = new LineImpl(value);
            assertFalse(l.hasRoles());
        }
    }

    @Nested
    @DisplayName("isCompletedLine()")
    class IsCompletedLine {
        @ParameterizedTest(name = "[{index}] value = {0}")
        @DisplayName("returns true")
        @ValueSource(strings = {
                "This is a completed line.",
                "This is a completed line!",
                "This is a completed line?",
                "This is a completed line…",
                "This is a completed line/",
                "This is a completed line”",
                "This is a completed line\"",
                "This is a completed line)"
        })
        void isCompletedLineTrue(String value) {
            LineImpl l = new LineImpl(value);
            assertTrue(l.isCompletedLine());
        }

        @ParameterizedTest(name = "[{index}] value = {0}")
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
            LineImpl l = new LineImpl(value);
            assertFalse(l.isCompletedLine());
        }
    }
}