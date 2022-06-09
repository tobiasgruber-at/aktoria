package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimplePageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleRoleDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleScriptDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.LineMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.PageMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.RoleMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ScriptMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.IllegalFileFormatException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.ScriptService;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.line.Line;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.line.impl.LineImpl;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.page.Page;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.page.impl.PageImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
class ScriptServiceImplIntegrationTest {

    @InjectMocks
    private ScriptMapper scriptMapper;
    @InjectMocks
    private RoleMapper roleMapper;
    @InjectMocks
    private PageMapper pageMapper;
    @InjectMocks
    private LineMapper lineMapper;

    @Autowired
    ScriptServiceImplIntegrationTest(ScriptMapper scriptMapper, RoleMapper roleMapper, PageMapper pageMapper, LineMapper lineMapper) {
        this.scriptMapper = scriptMapper;
        this.roleMapper = roleMapper;
        this.pageMapper = pageMapper;
        this.lineMapper = lineMapper;
    }

    @BeforeEach
    public void init() {
        ReflectionTestUtils.setField(lineMapper, "roleMapper", roleMapper);
        ReflectionTestUtils.setField(pageMapper, "lineMapper", lineMapper);
        ReflectionTestUtils.setField(scriptMapper, "pageMapper", pageMapper);
        ReflectionTestUtils.setField(scriptMapper, "roleMapper", roleMapper);
    }

    @Test
    @DirtiesContext
    @DisplayName("newScript() returns the correct DTO")
    void newScript() throws ServiceException, IOException, IllegalFileFormatException {
        final List<Line> expectedLines = new LinkedList<>();
        expectedLines.add(new LineImpl("Erster Akt", 0L));
        expectedLines.add(new LineImpl("Das ist eine Beschreibung der Örtlichkeit, wo sich der erste Akt abspielt. Diese Phrase soll keiner Rolle zugewiesen werden.", 0L));
        expectedLines.add(new LineImpl("ALICE Das ist die erste Phrase in diesem Theaterstück. Diese Phrase soll Alice zugeteilt werden.", 0L));
        expectedLines.add(new LineImpl("BOB Hallo Alice! Wie geht’s dir so? (Schaut Alice in die Augen)", 0L));
        expectedLines.add(new LineImpl("MR. MISTER Bla Bla Bla.", 0L));
        expectedLines.add(new LineImpl("(Anna tritt auf.)", 0L));
        expectedLines.add(new LineImpl("ANNA P. (fröhlich) Halli-hallöchen!", 0L));
        expectedLines.add(new LineImpl("LADY MARI-MUSTER O man. Ich brauch‘ erst mal einen Kaffee.", 0L));
        expectedLines.get(7).setConflictType(Line.ConflictType.VERIFICATION_REQUIRED);
        expectedLines.add(new LineImpl("ANNA P. UND BOB (gleichzeitig.) Ich auch!", 0L));
        expectedLines.add(new LineImpl("Zweiter Akt", 0L));
        expectedLines.add(new LineImpl("Vorhang", 0L));

        expectedLines.get(0).setIndex(0L);
        expectedLines.get(1).setIndex(1L);
        expectedLines.get(2).setIndex(2L);
        expectedLines.get(3).setIndex(3L);
        expectedLines.get(4).setIndex(4L);
        expectedLines.get(5).setIndex(5L);
        expectedLines.get(6).setIndex(6L);
        expectedLines.get(7).setIndex(7L);
        expectedLines.get(8).setIndex(8L);
        expectedLines.get(9).setIndex(9L);
        expectedLines.get(10).setIndex(10L);

        final List<Page> expectedPages = new LinkedList<>();
        expectedPages.add(new PageImpl(expectedLines, 0L));

        final List<String> expectedRoles = new LinkedList<>();
        expectedRoles.add("ALICE");
        expectedRoles.add("BOB");
        expectedRoles.add("MR. MISTER");
        expectedRoles.add("ANNA P.");
        expectedRoles.add("LADY MARI-MUSTER");

        final File f = new File("./src/test/resources/service/parsing/script/Skript_NF.pdf");
        final MultipartFile multipartFile = new MockMultipartFile("Skript_NF.pdf", new FileInputStream(f));
        final ScriptService scriptService = new ScriptServiceImpl(scriptMapper, null, null, null, null, null, null, null, null, null);

        final List<SimplePageDto> expectedPagesDto = pageMapper.listOfPageToListOfSimplePageDto(expectedPages);
        final List<SimpleRoleDto> expectedRolesDto = roleMapper.listOfStringToListOfSimpleRoleDto(expectedRoles);

        final SimpleScriptDto actual = scriptService.parse(multipartFile, 0);
        final SimpleScriptDto expected = new SimpleScriptDto(multipartFile.getOriginalFilename(), expectedPagesDto, expectedRolesDto);

        assertEquals(expected, actual);
    }
}