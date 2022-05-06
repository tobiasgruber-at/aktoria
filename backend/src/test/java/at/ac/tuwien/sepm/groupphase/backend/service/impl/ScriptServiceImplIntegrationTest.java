package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.StagedScriptDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.ScriptService;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.line.Line;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.line.impl.LineImpl;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.page.Page;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.page.impl.PageImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
class ScriptServiceImplIntegrationTest {

    @Test
    @DisplayName("newScript() returns the correct DTO")
    void newScript() throws ServiceException {
        List<Line> expectedLines = new LinkedList<>();
        expectedLines.add(new LineImpl("Erster Akt", 0));
        expectedLines.add(new LineImpl("Das ist eine Beschreibung der Örtlichkeit, wo sich der erste Akt abspielt. Diese Phrase soll keiner Rolle zugewiesen werden.", 0));
        expectedLines.add(new LineImpl("ALICE Das ist die erste Phrase in diesem Theaterstück. Diese Phrase soll Alice zugeteilt werden.", 0));
        expectedLines.add(new LineImpl("BOB Hallo Alice! Wie geht’s dir so? (Schaut Alice in die Augen)", 0));
        expectedLines.add(new LineImpl("MR. MISTER Bla Bla Bla.", 0));
        expectedLines.add(new LineImpl("(Anna tritt auf.)", 0));
        expectedLines.add(new LineImpl("ANNA P. (fröhlich) Halli-hallöchen!", 0));
        expectedLines.add(new LineImpl("LADY MARI-MUSTER O man. Ich brauch‘ erst mal einen Kaffee.", 0));
        expectedLines.get(7).setConflictType(Line.ConflictType.VERIFICATION_REQUIRED);
        expectedLines.add(new LineImpl("ANNA P. UND BOB (gleichzeitig.) Ich auch!", 0));
        expectedLines.add(new LineImpl("Zweiter Akt", 0));
        expectedLines.add(new LineImpl("Vorhang", 0));

        List<Page> expectedPages = new LinkedList<>();
        expectedPages.add(new PageImpl(expectedLines));

        List<String> expectedRoles = new LinkedList<>();
        expectedRoles.add("ALICE");
        expectedRoles.add("BOB");
        expectedRoles.add("MR. MISTER");
        expectedRoles.add("ANNA P.");
        expectedRoles.add("LADY MARI-MUSTER");

        File f = new File("./src/test/resources/service/parsing/script/Skript_NF.pdf");
        ScriptService scriptService = new ScriptServiceImpl();

        StagedScriptDto actual = scriptService.newScript(f);
        StagedScriptDto expected = new StagedScriptDto(expectedPages, expectedRoles);

        assertEquals(expected, actual);
    }
}