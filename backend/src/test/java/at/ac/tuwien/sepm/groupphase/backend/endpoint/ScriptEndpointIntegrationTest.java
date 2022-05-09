package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleLineDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimplePageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.StagedScriptDto;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.line.Line;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
        @Test
        @Transactional
        @DisplayName("returns the correctly parsed script")
        void uploadScriptReturnsCorrectly() throws Exception {

            final List<String> roles = new LinkedList<>();
            roles.add("ALICE");
            roles.add("BOB");
            roles.add("MR. MISTER");
            roles.add("ANNA P.");
            roles.add("LADY MARI-MUSTER");

            final List<SimpleLineDto> simpleLineDtos = new LinkedList<>();
            simpleLineDtos.add(new SimpleLineDto(null, "Erster Akt", "Erster Akt", null));
            simpleLineDtos.add(new SimpleLineDto(null, "Das ist eine Beschreibung der Örtlichkeit, wo sich der erste Akt abspielt. Diese Phrase soll keiner Rolle zugewiesen werden.",
                "Das ist eine Beschreibung der Örtlichkeit, wo sich der erste Akt abspielt. Diese Phrase soll keiner Rolle zugewiesen werden.", null));
            simpleLineDtos.add(new SimpleLineDto(Arrays.stream(new String[] {"ALICE"}).toList(), "Das ist die erste Phrase in diesem Theaterstück. Diese Phrase soll Alice zugeteilt werden.",
                "ALICE Das ist die erste Phrase in diesem Theaterstück. Diese Phrase soll Alice zugeteilt werden.", null));
            simpleLineDtos.add(new SimpleLineDto(Arrays.stream(new String[] {"BOB"}).toList(), "Hallo Alice! Wie geht’s dir so? (Schaut Alice in die Augen)", "BOB Hallo Alice! Wie geht’s dir so? (Schaut Alice in die Augen)", null));
            simpleLineDtos.add(new SimpleLineDto(Arrays.stream(new String[] {"MR. MISTER"}).toList(), "Bla Bla Bla.", "MR. MISTER Bla Bla Bla.", null));
            simpleLineDtos.add(new SimpleLineDto(null, "(Anna tritt auf.)", "(Anna tritt auf.)", null));
            simpleLineDtos.add(new SimpleLineDto(Arrays.stream(new String[] {"ANNA P."}).toList(), "(fröhlich) Halli-hallöchen!", "ANNA P. (fröhlich) Halli-hallöchen!", null));
            simpleLineDtos.add(new SimpleLineDto(Arrays.stream(new String[] {"LADY MARI-MUSTER"}).toList(), "O man. Ich brauch‘ erst mal einen Kaffee.", "LADY MARI-MUSTER O man. Ich brauch‘ erst mal einen Kaffee.",
                Line.ConflictType.VERIFICATION_REQUIRED));
            simpleLineDtos.add(new SimpleLineDto(Arrays.stream(new String[] {"ANNA P.", "BOB"}).toList(), "(gleichzeitig.) Ich auch!", "ANNA P. UND BOB (gleichzeitig.) Ich auch!", null));
            simpleLineDtos.add(new SimpleLineDto(null, "Zweiter Akt", "Zweiter Akt", null));
            simpleLineDtos.add(new SimpleLineDto(null, "Vorhang", "Vorhang", null));

            final List<SimplePageDto> simplePageDtos = new LinkedList<>();
            simplePageDtos.add(new SimplePageDto(simpleLineDtos));

            final StagedScriptDto expected = new StagedScriptDto(simplePageDtos, roles);

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

            StagedScriptDto response = objectMapper.readValue(body, StagedScriptDto.class);

            assertThat(response).isNotNull();
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