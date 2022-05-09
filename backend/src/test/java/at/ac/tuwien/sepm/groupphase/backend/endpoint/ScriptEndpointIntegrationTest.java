package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleLineDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimplePageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleRoleDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleScriptDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.PageMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.PageMapperImpl;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.RoleMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.line.Line;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles({"test", "datagen"})
@EnableWebMvc
@WebAppConfiguration
class ScriptEndpointIntegrationTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext webAppContext;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Disabled
    @Test
    @Transactional
    @DisplayName("saveScript() saves the script correctly")
    void saveScript() {
    }

    @Nested
    @DisplayName("uploadScript() ")
    class UploadScript {

        @InjectMocks
        private PageMapper pageMapper = PageMapperImpl.INSTANCE;

        @BeforeEach
        public void init() {
            RoleMapper roleMapper = Mappers.getMapper(RoleMapper.class);
            ReflectionTestUtils.setField(pageMapper, "roleMapper", roleMapper);
        }

        @Test
        @Transactional
        @DisplayName("returns the correctly parsed script")
        void uploadScriptReturnsCorrectly() throws Exception {

            final List<SimpleRoleDto> expectedRolesDto = new LinkedList<>();
            expectedRolesDto.add(new SimpleRoleDto("ALICE", null));
            expectedRolesDto.add(new SimpleRoleDto("BOB", null));
            expectedRolesDto.add(new SimpleRoleDto("MR. MISTER", null));
            expectedRolesDto.add(new SimpleRoleDto("ANNA P.", null));
            expectedRolesDto.add(new SimpleRoleDto("LADY MARI-MUSTER", null));

            final List<SimpleLineDto> simpleLinesDto = new LinkedList<>();
            simpleLinesDto.add(
                new SimpleLineDto(
                    0L,
                    null,
                    "Erster Akt",
                    true,
                    null
                ));
            simpleLinesDto.add(
                new SimpleLineDto(
                    1L,
                    null,
                    "Das ist eine Beschreibung der Örtlichkeit, wo sich der erste Akt abspielt. Diese Phrase soll keiner Rolle zugewiesen werden.",
                    true,
                    null
                ));
            simpleLinesDto.add(
                new SimpleLineDto(
                    2L,
                    List.of(new SimpleRoleDto[] { new SimpleRoleDto("ALICE", null) }),
                    "Das ist die erste Phrase in diesem Theaterstück. Diese Phrase soll Alice zugeteilt werden.",
                    true,
                    null)
            );
            simpleLinesDto.add(
                new SimpleLineDto(
                    3L,
                    List.of(new SimpleRoleDto[] { new SimpleRoleDto("BOB", null) }),
                    "Hallo Alice! Wie geht’s dir so? (Schaut Alice in die Augen)",
                    true,
                    null
                ));
            simpleLinesDto.add(
                new SimpleLineDto(
                    4L,
                    List.of(new SimpleRoleDto[] { new SimpleRoleDto("MR. MISTER", null) }),
                    "Bla Bla Bla.",
                    true,
                    null
                ));
            simpleLinesDto.add(
                new SimpleLineDto(
                    5L,
                    null,
                    "(Anna tritt auf.)",
                    true,
                    null
                ));
            simpleLinesDto.add(
                new SimpleLineDto(
                    6L,
                    List.of(new SimpleRoleDto[] { new SimpleRoleDto("ANNA P.", null) }),
                    "(fröhlich) Halli-hallöchen!",
                    true,
                    null
                ));
            simpleLinesDto.add(
                new SimpleLineDto(
                    7L,
                    List.of(new SimpleRoleDto[] { new SimpleRoleDto("LADY MARI-MUSTER", null) }),
                    "O man. Ich brauch‘ erst mal einen Kaffee.",
                    true,
                    Line.ConflictType.VERIFICATION_REQUIRED
                ));
            simpleLinesDto.add(
                new SimpleLineDto(
                    8L,
                    List.of(new SimpleRoleDto[] { new SimpleRoleDto("ANNA P.", null), new SimpleRoleDto("BOB", null) }),
                    "(gleichzeitig.) Ich auch!",
                    true,
                    null
                ));
            simpleLinesDto.add(
                new SimpleLineDto(
                    9L,
                    null,
                    "Zweiter Akt",
                    true,
                    null
                ));
            simpleLinesDto.add(
                new SimpleLineDto(
                    10L,
                    null,
                    "Vorhang",
                    true,
                    null
                ));

            simpleLinesDto.get(0).setIndex(0L);
            simpleLinesDto.get(1).setIndex(1L);
            simpleLinesDto.get(2).setIndex(2L);
            simpleLinesDto.get(3).setIndex(3L);
            simpleLinesDto.get(4).setIndex(4L);
            simpleLinesDto.get(5).setIndex(5L);
            simpleLinesDto.get(6).setIndex(6L);
            simpleLinesDto.get(7).setIndex(7L);
            simpleLinesDto.get(8).setIndex(8L);
            simpleLinesDto.get(9).setIndex(9L);
            simpleLinesDto.get(10).setIndex(10L);

            final List<SimplePageDto> simplePagesDto = new LinkedList<>();
            simplePagesDto.add(new SimplePageDto(simpleLinesDto, 0L));

            final SimpleScriptDto expected = new SimpleScriptDto("name", simplePagesDto, expectedRolesDto);

            File pdf = new File("./src/test/resources/service/parsing/script/Skript_NF.pdf");
            MockMultipartFile multipartFile = new MockMultipartFile("file", pdf.getName(), MediaType.APPLICATION_PDF_VALUE, new FileInputStream(pdf));

            byte[] body = mockMvc
                .perform(MockMvcRequestBuilders
                    .multipart("/api/v1/scripts/new")
                    .file(multipartFile)
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsByteArray();

            SimpleScriptDto response = objectMapper.readValue(body, SimpleScriptDto.class);

            assertNotNull(response);
            assertEquals(expected, response);
        }

        @Test
        @DisplayName("returns correct status code for corrupted files")
        @Transactional
        void uploadScriptReturnsCorrectStatusCodeForCorruptedFiles() throws Exception {
            File pdf = new File("./src/test/resources/service/parsing/script/Skript_NF_CORRUPTED.pdf");
            MockMultipartFile multipartFile = new MockMultipartFile("file", pdf.getName(), MediaType.APPLICATION_PDF_VALUE, new FileInputStream(pdf));

            mockMvc
                .perform(MockMvcRequestBuilders
                    .multipart("/api/v1/scripts/new")
                    .file(multipartFile)
                ).andExpect(status().isUnprocessableEntity());
        }
    }
}