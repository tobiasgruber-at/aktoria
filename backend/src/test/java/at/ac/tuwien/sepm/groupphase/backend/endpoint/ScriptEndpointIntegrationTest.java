package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.StagedScriptDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles({ "test", "datagen" })
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
        }

        @Disabled
        @ParameterizedTest
        @Transactional
        @DisplayName("returns the correct status codes for different media types")
        @ValueSource(strings = {
            MediaType.TEXT_PLAIN_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_JSON_VALUE
        })
        void uploadScriptReturnsCorrectStatusCodeForDifferentMediaTypes(String input) throws Exception {

            File pdf = new File("./src/test/resources/service/parsing/script/Skript_NF.pdf");
            MockMultipartFile multipartFile = new MockMultipartFile("file", pdf.getName(), input, new FileInputStream(pdf));

            mockMvc
                .perform(MockMvcRequestBuilders
                    .multipart("/api/v1/scripts/new")
                    .file(multipartFile)
                ).andExpect(status().isUnsupportedMediaType());
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