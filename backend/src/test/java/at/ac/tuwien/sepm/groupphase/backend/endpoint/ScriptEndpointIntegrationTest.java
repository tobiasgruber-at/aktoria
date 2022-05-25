package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.datagenerator.ScriptDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.UserDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LineDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RoleDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ScriptDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ScriptPreviewDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleColorDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleLineDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimplePageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleRoleDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleScriptDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ScriptMapper;
import at.ac.tuwien.sepm.groupphase.backend.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.exception.UnauthorizedException;
import at.ac.tuwien.sepm.groupphase.backend.service.ScriptService;
import at.ac.tuwien.sepm.groupphase.backend.testhelpers.ScriptTestHelper;
import at.ac.tuwien.sepm.groupphase.backend.testhelpers.UserTestHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private ScriptService scriptService;
    @Autowired
    private ScriptMapper scriptMapper;
    @Autowired
    private ScriptTestHelper scriptTestHelper;
    @Autowired
    private UserTestHelper userTestHelper;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    @Transactional
    @DisplayName("saveScript() saves the script correctly")
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = { Role.verified })
    void saveScript() {
        SimpleRoleDto simpleRoleDto = new SimpleRoleDto();
        simpleRoleDto.setName("ROLEA");
        simpleRoleDto.setColor(new SimpleColorDto(255, 0, 255));

        SimpleLineDto simpleLineDto = new SimpleLineDto();
        simpleLineDto.setIndex(0L);
        simpleLineDto.setRoles(List.of(simpleRoleDto));
        simpleLineDto.setContent("This is the content of the line");
        simpleLineDto.setActive(true);
        simpleLineDto.setConflictType(null);

        SimplePageDto simplePageDto = new SimplePageDto();
        simplePageDto.setLines(List.of(simpleLineDto));
        simplePageDto.setIndex(0L);

        SimpleUserDto simpleUserDto = new SimpleUserDto();
        simpleUserDto.setId(-1L);
        simpleUserDto.setEmail(UserTestHelper.dummyUserEmail);
        simpleUserDto.setFirstName("firstname");
        simpleUserDto.setLastName("lastname");
        simpleUserDto.setVerified(true);

        SimpleScriptDto simpleScriptDto = new SimpleScriptDto();
        simpleScriptDto.setName("Test Script");
        simpleScriptDto.setPages(List.of(simplePageDto));

        RoleDto roleDto = new RoleDto();
        roleDto.setName("ROLEA");
        roleDto.setColor(new SimpleColorDto(255, 0, 255));

        LineDto lineDto = new LineDto();
        lineDto.setActive(true);
        lineDto.setIndex(0L);
        lineDto.setConflictType(null);
        lineDto.setRoles(List.of(roleDto));
        lineDto.setContent("This is the content of the line");

        PageDto pageDto = new PageDto();
        pageDto.setLines(List.of(lineDto));
        pageDto.setIndex(0L);

        SimpleUserDto simpleUserDto2 = new SimpleUserDto();
        simpleUserDto2.setId(-1L);
        simpleUserDto2.setEmail(UserTestHelper.dummyUserEmail);
        simpleUserDto2.setFirstName("firstname");
        simpleUserDto2.setLastName("lastname");
        simpleUserDto2.setVerified(true);

        ScriptDto scriptDto = new ScriptDto();
        scriptDto.setName("Test Script");
        scriptDto.setPages(List.of(pageDto));

        ScriptDto saved = scriptService.save(simpleScriptDto);

        scriptDto.setId(saved.getId());
        scriptDto.setOwner(saved.getOwner());

        assertEquals(scriptDto, saved);
    }

    @Test
    @Transactional
    @DisplayName("getScriptPreviews() gets zero previews for user with no scripts")
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = { Role.verified })
    void getScriptPreviewsReturnsZero() {
        List<ScriptPreviewDto> scriptPreviewDtoList = scriptService.findAllPreviews().toList();
        assertEquals(0, scriptPreviewDtoList.size());
    }

    @Test
    @Transactional
    @DisplayName("getScriptPreviews() gets the correct previews")
    @WithMockUser(username = UserDataGenerator.TEST_USER_EMAIL_LOCAL + "2" + UserDataGenerator.TEST_USER_EMAIL_DOMAIN, password = UserDataGenerator.TEST_USER_PASSWORD + "2", roles = { Role.verified })
    void getScriptPreviews() {
        List<ScriptPreviewDto> scriptPreviewDtoList = scriptService.findAllPreviews().toList();
        assertEquals(new ScriptPreviewDto(1L, ScriptDataGenerator.TEST_SCRIPT_NAME + " 1"), scriptPreviewDtoList.get(0));
        assertEquals(Math.floorDiv(ScriptDataGenerator.NUMBER_OF_SCRIPTS_TO_GENERATE, UserDataGenerator.NUMBER_OF_USERS_TO_GENERATE) + 1, scriptPreviewDtoList.size());
    }

    @Test
    @Transactional
    @WithMockUser(username = UserDataGenerator.TEST_USER_EMAIL_LOCAL + "2" + UserDataGenerator.TEST_USER_EMAIL_DOMAIN, password = UserDataGenerator.TEST_USER_PASSWORD + "2", roles = { Role.verified })
    @DisplayName("getScriptById() gets the correct script")
    void getScriptById() {
        ScriptDto scriptDto = scriptService.findById(1L);
        assertNotNull(scriptDto);
    }

    @Test
    @Transactional
    @WithMockUser(username = UserDataGenerator.TEST_USER_EMAIL_LOCAL + "1" + UserDataGenerator.TEST_USER_EMAIL_DOMAIN, password = UserDataGenerator.TEST_USER_PASSWORD + "2", roles = { Role.verified })
    @DisplayName("getScriptById() throws UnauthorizedException")
    void getScriptByIdThrowsException() {
        assertThrows(UnauthorizedException.class, () -> scriptService.findById(1L));
    }

    @Nested
    @DisplayName("parseScript() ")
    class SaveScript {
        @Test
        @Transactional
        @DisplayName("returns the saved script")
        @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = { Role.verified })
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
        @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = { Role.user })
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
        @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = { Role.verified })
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
        @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = { Role.verified })
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