package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SectionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleSectionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.LineMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.SectionMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Line;
import at.ac.tuwien.sepm.groupphase.backend.entity.Script;
import at.ac.tuwien.sepm.groupphase.backend.entity.Section;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.repository.LineRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ScriptRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectionRepository;
import at.ac.tuwien.sepm.groupphase.backend.testhelpers.UserTestHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Class for testing Section Endpoint.
 *
 * @author Julia Bernold
 */
@SpringBootTest
@ActiveProfiles({ "test", "datagen" })
@EnableWebMvc
@WebAppConfiguration
class SectionEndpointIntegrationTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    SectionRepository sectionRepository;
    @Autowired
    SectionMapper sectionMapper;
    @Autowired
    private WebApplicationContext webAppContext;
    private MockMvc mockMvc;
    @Autowired
    private ScriptRepository scriptRepository;
    private List<Script> scriptList;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private LineMapper lineMapper;
    @Autowired
    private LineRepository lineRepository;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
        this.scriptList = scriptRepository.findAll();
    }

    //TESTING GET
    @Nested
    @DisplayName("getSectionById()")
    class GetSectionById {
        @Test
        @DirtiesContext
        @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = Role.verified)
        @DisplayName("Get a Section with a valid Id")
        void getSectionById() throws Exception {
            Optional<Section> input = sectionRepository.findById(1L);
            assertTrue(input.isPresent());
            byte[] body = mockMvc
                .perform(MockMvcRequestBuilders
                    .get("/api/v1/sections/" + input.get().getId())
                    .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();
            SectionDto sectionResult = objectMapper.readValue(body, SectionDto.class);
            assertNotNull(sectionResult);
            assertEquals(1L, sectionResult.getId());
        }

        @Test
        @DirtiesContext
        @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = Role.verified)
        @DisplayName("Get a Section with invalid Id")
        void getSectionInvalidId() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/sections/{id}", -1L)
                .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isNotFound());
        }
    }

    //TESTING GET ALL
    @Nested
    @DisplayName("getAllSections()")
    class GetAllSections {
        @Test
        @DirtiesContext
        @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = Role.verified)
        @DisplayName("Get all Sections")
        void getSectionById() throws Exception {
            //List<SectionDto> expected = sectionMapper.sectionListToSectionDtoList(sectionRepository.findAll());
            byte[] body = mockMvc
                .perform(MockMvcRequestBuilders
                    .get("/api/v1/sections?scriptId=1")
                    .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();
            List<SectionDto> sectionResult = objectMapper.readValue(body, new TypeReference<List<SectionDto>>() {
            });
            assertNotNull(sectionResult);
        }
    }

    //TESTING POST
    @Nested
    @DisplayName("saveSection()")
    class SaveSection {
        @Test
        @DirtiesContext
        @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = Role.verified)
        @DisplayName("Save a section successfully")
        void saveSection() throws Exception {
            Script script = scriptList.get(0);
            User user = script.getOwner();
            byte[] body = mockMvc
                .perform(MockMvcRequestBuilders
                    .post("/api/v1/sections")
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(new SimpleSectionDto("Szene Fünf", user.getId(), 1L, 3L, null)))
                    .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsByteArray();
            SectionDto sectionResult = objectMapper.readValue(body, SectionDto.class);

            Optional<Line> startLineOptional = lineRepository.findById(1L);
            assertTrue(startLineOptional.isPresent());
            Optional<Line> endLineOptional = lineRepository.findById(3L);
            assertTrue(endLineOptional.isPresent());

            SectionDto expected = new SectionDto(
                sectionResult.getId(),
                "Szene Fünf",
                userMapper.userToSimpleUserDto(user),
                lineMapper.lineToLineDto(startLineOptional.get()),
                lineMapper.lineToLineDto(endLineOptional.get()),
                null
            );

            assertNotNull(sectionResult);
            assertEquals(expected, sectionResult);
        }

        @Test
        @DirtiesContext
        @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = Role.verified)
        @DisplayName("Save a section without a user")
        void saveSectionNoUser() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/sections")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new SimpleSectionDto("Szene Fünf", 0L, 1L, 3L, null)))
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isUnprocessableEntity());
        }

        @Test
        @DirtiesContext
        @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = Role.verified)
        @DisplayName("Save a section with an invalid starting line")
        void saveSectionInvalidStart() throws Exception {
            Script script = scriptList.get(0);
            User user = script.getOwner();
            mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/sections")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new SimpleSectionDto("Szene Fünf", user.getId(), 0L, 1L, null)))
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isUnprocessableEntity());
        }

        @Test
        @DirtiesContext
        @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = Role.verified)
        @DisplayName("Save a section with an invalid ending line")
        void saveSectionInvalidEnd() throws Exception {
            Script script = scriptList.get(0);
            User user = script.getOwner();
            //before the first page, and the start is "below" the end
            mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/sections")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new SimpleSectionDto("Szene Fünf", user.getId(), 1L, -2L, null)))
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isUnprocessableEntity());
        }

        @Test
        @DirtiesContext
        @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = Role.verified)
        @DisplayName("Save a section with two lines from different scripts")
        void saveSectionFalseScript() throws Exception {
            Script script = scriptList.get(0);
            User user = script.getOwner();
            //lines not from the same script
            mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/sections")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new SimpleSectionDto("Szene Fünf", user.getId(), 1L, 200L, null)))
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isUnprocessableEntity());
        }

        @Test
        @DirtiesContext
        @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = Role.verified)
        @DisplayName("Save a section with too long name")
        void saveSectionTooLongName() throws Exception {
            Script script = scriptList.get(0);
            User user = script.getOwner();
            mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/sections")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new SimpleSectionDto("a".repeat(101), user.getId(), 1L, 80L, null)))
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isUnprocessableEntity());
        }

        @Test
        @DirtiesContext
        @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = Role.verified)
        @DisplayName("Save a section with blank name")
        void saveSectionBlankName() throws Exception {
            Script script = scriptList.get(0);
            User user = script.getOwner();
            mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/sections")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new SimpleSectionDto("    ", user.getId(), 1L, 20L, null)))
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isUnprocessableEntity());
        }

        @Test
        @DirtiesContext
        @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = Role.verified)
        @DisplayName("Save a section with lines from a nonexistent script")
        void saveSectionNonexistentScript() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/sections")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new SimpleSectionDto("Szene Fünf", 1L, -1L, -2L, null)))
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isUnprocessableEntity());
        }

        @Test
        @DirtiesContext
        @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = Role.verified)
        @DisplayName("Save a section with a starting line that is after the ending line")
        void saveSectionStartAfterEnd() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/sections")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new SimpleSectionDto("Szene Fünf", 1L, 20L, 2L, null)))
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isUnprocessableEntity());
        }
    }

    //TESTING DELETE
    @Nested
    @DisplayName("deleteSection()")
    class DeleteSection {
        @Test
        @DirtiesContext
        @DisplayName("Delete a section")
        @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = Role.verified)
        void deleteSection() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/v1/sections/1")
                .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isNoContent());
        }

        @Test
        @DirtiesContext
        @DisplayName("Delete a section with invalid Id")
        @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = Role.verified)
        void deleteSectionInvalidId() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/v1/sections/0")
                .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isNotFound());
        }
    }
}