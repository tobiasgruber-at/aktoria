package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SectionDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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

    //TESTING POST
    @Nested
    @DisplayName("saveSection()")
    class SaveSection {
        @Test
        @Transactional
        @DisplayName("Save a section")
        void saveSection() throws Exception {
            //TODO
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
    }

    //TESTING GET
    @Nested
    @DisplayName("getSectionById()")
    class GetSectionById {
        @Test
        @Transactional
        @DisplayName("Get a Section with a valid ID")
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
    }
}