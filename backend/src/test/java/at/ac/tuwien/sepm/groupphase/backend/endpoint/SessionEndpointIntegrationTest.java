package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SessionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleSessionDto;
import at.ac.tuwien.sepm.groupphase.backend.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectionRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.SessionService;
import at.ac.tuwien.sepm.groupphase.backend.testhelpers.UserTestHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles({ "datagen", "test" })
@EnableWebMvc
@WebAppConfiguration
public class SessionEndpointIntegrationTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext webAppContext;
    private MockMvc mockMvc;
    @Autowired
    private UserTestHelper userTestHelper;
    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private SessionService sessionService;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    @Transactional
    @DisplayName("saveSession() saves the session correctly")
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = { Role.verified })
    public void saveSession() {
        SimpleSessionDto simpleSessionDto = new SimpleSessionDto();
        simpleSessionDto.setSectionId(1L);
        simpleSessionDto.setRoleId(1L);

        SessionDto actual = sessionService.save(simpleSessionDto);
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getCoverage()).isEqualTo(0.0);
        assertThat(actual.getDeprecated()).isEqualTo(false);
        assertThat(actual.getStart()).isNotNull();
        assertThat(actual.getEnd()).isNull();
        assertThat(actual.getSectionId()).isEqualTo(1L);
        assertThat(actual.getRoleId()).isEqualTo(1L);
        assertThat(actual.getCurrentLineId()).isNotNull();
    }

    @Test
    @Transactional
    @DisplayName("return saved session correctly")
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = { Role.verified })
    public void startSessionCorrectly() throws Exception {
        SimpleSessionDto simpleSessionDto = new SimpleSessionDto();
        simpleSessionDto.setSectionId(1L);
        simpleSessionDto.setRoleId(1L);

        byte[] body = mockMvc
            .perform(MockMvcRequestBuilders
                .post(SessionEndpoint.path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(simpleSessionDto))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsByteArray();

        SessionDto actual = objectMapper.readValue(body, SessionDto.class);
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getCoverage()).isEqualTo(0.0);
        assertThat(actual.getDeprecated()).isEqualTo(false);
        assertThat(actual.getStart()).isNotNull();
        assertThat(actual.getEnd()).isNull();
        assertThat(actual.getSectionId()).isEqualTo(1L);
        assertThat(actual.getRoleId()).isEqualTo(1L);
        assertThat(actual.getCurrentLineId()).isNotNull();
    }

    @Test
    @Transactional
    @DisplayName("saving throws unprocessable entity exception")
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = { Role.verified })
    public void startSessionThrowsUnprocessableEntityException() throws Exception {
        SimpleSessionDto simpleSessionDto = new SimpleSessionDto();
        simpleSessionDto.setSectionId(-1L);
        simpleSessionDto.setRoleId(-1L);

        mockMvc.perform(MockMvcRequestBuilders
                .post(SessionEndpoint.path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(simpleSessionDto))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnprocessableEntity());
    }
}
