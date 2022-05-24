package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ScriptDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleScriptDto;
import at.ac.tuwien.sepm.groupphase.backend.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.testhelpers.ScriptTestHelper;
import at.ac.tuwien.sepm.groupphase.backend.testhelpers.UserTestHelper;
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
import org.springframework.security.test.context.support.WithMockUser;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    @Autowired
    private ScriptTestHelper scriptTestHelper;
    @Autowired
    private UserTestHelper userTestHelper;

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

    @Disabled
    @Test
    @Transactional
    @DisplayName("getScriptPreviews() gets the correct previews")
    void getScriptPreviews() {
    }

    @Disabled
    @Test
    @Transactional
    @DisplayName("getScriptById() gets the correct script")
    void getScriptById() {
    }

    @Nested
    @DisplayName("parseScript() ")
    class SaveScript {
        @Test
        @Transactional
        @DisplayName("returns the saved script")
        @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = Role.verified)
        void saveScriptReturnsCorrectly() throws Exception {
            byte[] body = mockMvc
                .perform(MockMvcRequestBuilders
                    .post(ScriptEndpoint.path)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(scriptTestHelper.dummySimpleScriptDto()))
                    .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsByteArray();
            ScriptDto savedScript = objectMapper.readValue(body, ScriptDto.class);
            ScriptDto expected = scriptTestHelper.dummyScriptDto(savedScript.getId(), userTestHelper.dummyUserDto());
            assertEquals(expected, savedScript);
        }

        @Test
        @Transactional
        @DisplayName("returns correct status code for invalid body")
        @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = Role.user)
        void saveScriptReturnsCorrectStatusCodeForInvalidInputs() throws Exception {
            mockMvc
                .perform(MockMvcRequestBuilders
                    .post(ScriptEndpoint.path)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\n"
                        + "    \"id\": 2,\n"
                        + "    \"name\": \"Stück\",\n"
                        + "    \"pages\": [")
                    .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().is4xxClientError());
        }
    }

    @Nested
    @DisplayName("uploadScript() ")
    class UploadScript {
        @Test
        @Transactional
        @DisplayName("returns the correctly parsed script")
        @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = Role.verified)
        void uploadScriptReturnsCorrectly() throws Exception {
            final SimpleScriptDto expected = scriptTestHelper.dummySimpleScriptDtoWithoutColors();

            final File pdf = new File("./src/test/resources/service/parsing/script/Skript_NF.pdf");
            final MockMultipartFile multipartFile = new MockMultipartFile("file", pdf.getName(), MediaType.APPLICATION_PDF_VALUE, new FileInputStream(pdf));

            byte[] body = mockMvc
                .perform(MockMvcRequestBuilders
                    .multipart("/api/v1/scripts/new")
                    .file(multipartFile)
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsByteArray();

            final SimpleScriptDto response = objectMapper.readValue(body, SimpleScriptDto.class);

            assertNotNull(response);
            assertEquals(expected, response);
        }

        @Test
        @Transactional
        @DisplayName("returns correct status code for corrupted files")
        @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = Role.verified)
        void uploadScriptReturnsCorrectStatusCodeForCorruptedFiles() throws Exception {
            final File pdf = new File("./src/test/resources/service/parsing/script/Skript_NF_CORRUPTED.pdf");
            final MockMultipartFile multipartFile = new MockMultipartFile("file", pdf.getName(), MediaType.APPLICATION_PDF_VALUE, new FileInputStream(pdf));

            mockMvc
                .perform(MockMvcRequestBuilders
                    .multipart("/api/v1/scripts/new")
                    .file(multipartFile)
                ).andExpect(status().isUnprocessableEntity());
        }
    }
}