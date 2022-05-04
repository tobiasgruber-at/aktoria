package at.ac.tuwien.sepm.groupphase.backend.service.parsing.line;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
class LineImplUnitTest {

    @Nested
    @DisplayName("hasRoles()")
    class HasRoles {
        @ParameterizedTest
        @DisplayName("returns true")
        @ValueSource(strings = {
                "ROLE This is my text.",
                "NAME SURENAME This is my text.",
                "ROLEA UND ROLEB This is my text."
        })
        void hasRolesTrue(String value) {
            LineImpl l = new LineImpl(value);
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
            LineImpl l = new LineImpl(value);
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
                "This is a completed line)"
        })
        void isCompletedLineTrue(String value) {
            LineImpl l = new LineImpl(value);
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
            LineImpl l = new LineImpl(value);
            assertFalse(l.isCompletedLine());
        }
    }
}