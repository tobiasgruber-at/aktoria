package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SectionDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Line;
import at.ac.tuwien.sepm.groupphase.backend.entity.Page;
import at.ac.tuwien.sepm.groupphase.backend.entity.Script;
import at.ac.tuwien.sepm.groupphase.backend.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.testhelpers.UserTestHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles({"test", "datagen"})
@EnableWebMvc
@WebAppConfiguration
class SectionEndpointIntegrationTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext webAppContext;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    //TESTING GET
    @Nested
    @DisplayName("getSectionById()")
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = Role.verified)
    class GetSectionById {
        @Test
        @Transactional
        @DisplayName("Get a Section with a valid Id")
        void getSectionById() throws Exception {
            byte[] body = mockMvc
                .perform(MockMvcRequestBuilders
                    .get("/api/v1/scripts/sections/-1")
                    .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();
            SectionDto sectionResult = objectMapper.readValue(body, SectionDto.class);
            assertNotNull(sectionResult);
            assertEquals(-1L, sectionResult.getId());
        }

        @Test
        @Transactional
        @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = Role.verified)
        @DisplayName("Get a Section with invalid Id")
        void getSectionInvalidId() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/scripts/sections/0")
                .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isNotFound());
        }
    }

    //TESTING POST
    @Nested
    @DisplayName("saveSection()")
    class SaveSection {
        @Test
        @Transactional
        @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = Role.verified)
        @DisplayName("Save a section successfully")
        void saveSection() throws Exception {
            //TODO: replace with actual dummy data
            Page page = new Page();
            Line start = new Line(null, page, -1L, "A", null, false, null, null);
            Line end = new Line(null, page, -2L, "B", null, false, null, null);
            byte[] body = mockMvc
                .perform(MockMvcRequestBuilders
                    .post("/api/v1/scripts/sections")
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(new SectionDto(null, "Szene Fünf", start, end)))
                    .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsByteArray();
            SectionDto sectionResult = objectMapper.readValue(body, SectionDto.class);
            SectionDto expected = new SectionDto(sectionResult.getId(), "Szene Fünf", start, end);

            assertNotNull(sectionResult);
            assertEquals(expected, sectionResult);
        }


        @Test
        @Transactional
        @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = Role.verified)
        @DisplayName("Save a section with an invalid starting line")
        void saveSectionInvalidStart() throws Exception {
            //TODO: replace with actual dummy data
            Page page = new Page();
            Line start = new Line(null, page, -10000000L, "A", null, false, null, null);
            Line end = new Line(null, page, -2L, "B", null, false, null, null);
            //there are not that many lines on the page, and the start is "below" the end
            mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/scripts/sections")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new SectionDto(null, "Szene Fünf", start, end)))
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isUnprocessableEntity());
        }


        @Test
        @Transactional
        @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = Role.verified)
        @DisplayName("Save a section with an invalid ending line")
        void saveSectionInvalidEnd() throws Exception {
            //TODO: replace with actual dummy data
            Script script = Script.builder().build();
            Page page1 = new Page(null, script, -1L, null);
            Page page2 = new Page(null, script, -100000L, null);
            Line start = new Line(null, page1, -1L, "A", null, false, null, null);
            Line end = new Line(null, page2, -2L, "B", null, false, null, null);
            //end is "after" the last page
            mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/scripts/sections")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new SectionDto(null, "Szene Fünf", start, end)))
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isUnprocessableEntity());
        }

        @Test
        @Transactional
        @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = Role.verified)
        @DisplayName("Save a section with two lines from different scripts")
        void saveSectionFalseScript() throws Exception {
            //TODO: replace with actual dummy data
            Script script = Script.builder().build();
            Script script1 = Script.builder().build();
            Page page1 = new Page(null, script, -1L, null);
            Page page2 = new Page(null, script1, -1L, null);
            Line start = new Line(null, page1, -1L, "A", null, false, null, null);
            Line end = new Line(null, page2, -2L, "B", null, false, null, null);
            //lines not from the same script
            mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/scripts/sections")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new SectionDto(null, "Szene Fünf", start, end)))
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isUnprocessableEntity());
        }

        @Test
        @Transactional
        @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = Role.verified)
        @DisplayName("Save a section with too long name")
        void saveSectionTooLongName() throws Exception {
            //TODO: replace with actual dummy data
            Page page = new Page();
            Line start = new Line(null, page, -1L, "A", null, false, null, null);
            Line end = new Line(null, page, -2L, "B", null, false, null, null);
            mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/scripts/sections")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new SectionDto(null, "a".repeat(101), start, end)))
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isUnprocessableEntity());
        }


        @Test
        @Transactional
        @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = Role.verified)
        @DisplayName("Save a section with lines from a nonexistent script")
        void saveSectionNonexistentScript() throws Exception {
            //TODO: replace with actual dummy data
            Script script = new Script(-10000L, "SCRIPT", null, null, null, null);
            Page page1 = new Page(null, script, -1L, null);
            Line start = new Line(null, page1, -1L, "A", null, false, null, null);
            Line end = new Line(null, page1, -2L, "B", null, false, null, null);
            //lines not from the same script
            mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/scripts/sections")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new SectionDto(null, "Szene Fünf", start, end)))
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isUnprocessableEntity());
        }
    }


    //TESTING DELETE
    @Nested
    @DisplayName("deleteSection()")
    class DeleteSection {
        @Test
        @Transactional
        @DisplayName("Delete a section")
        void deleteSection() throws Exception {
            //TODO
        }


        /*
          TODO:
          - missing ID
          - invalid ID
          */
    }


}