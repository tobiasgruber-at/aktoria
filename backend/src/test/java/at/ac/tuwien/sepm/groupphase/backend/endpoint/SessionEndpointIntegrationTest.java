package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SessionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleSessionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UpdateSessionDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Line;
import at.ac.tuwien.sepm.groupphase.backend.entity.Session;
import at.ac.tuwien.sepm.groupphase.backend.enums.AssessmentType;
import at.ac.tuwien.sepm.groupphase.backend.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.repository.LineRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectionRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SessionRepository;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles({"datagen", "test"})
@EnableWebMvc
@WebAppConfiguration
public class SessionEndpointIntegrationTest {

    @Autowired
    private WebApplicationContext webAppContext;
    private MockMvc mockMvc;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private LineRepository lineRepository;
    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    @Transactional
    @DisplayName("return saved session correctly and 201")
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = { Role.verified})
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

        SessionDto result = objectMapper.readValue(body, SessionDto.class);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getCoverage()).isEqualTo(0.0);
        assertThat(result.getDeprecated()).isEqualTo(false);
        assertThat(result.getStart()).isNotNull();
        assertThat(result.getEnd()).isNull();
        assertThat(result.getSectionId()).isEqualTo(1L);
        assertThat(result.getRoleId()).isEqualTo(1L);
        assertThat(result.getCurrentLineId()).isNotNull();
    }

    @Test
    @Transactional
    @DisplayName("saving throws unprocessable entity exception and returns 422")
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = { Role.verified})
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

    @Test
    @Transactional
    @DisplayName("returns updated session correctly and  200")
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = { Role.verified})
    public void updateSessionCorrectly() throws Exception {
        Optional<Session> sessionOpt = sessionRepository.findById(1L);
        if(sessionOpt.isPresent()) {
            Long curLineId = sessionOpt.get().getCurrentLine().getId() + 1;
            UpdateSessionDto updateSessionDto = new UpdateSessionDto();
            updateSessionDto.setDeprecated(true);
            updateSessionDto.setSelfAssessment(AssessmentType.poor);
            updateSessionDto.setCurrentLineId(curLineId);

            byte[] body = mockMvc
                .perform(MockMvcRequestBuilders
                    .patch(SessionEndpoint.path + "/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(updateSessionDto))
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();

            SessionDto result = objectMapper.readValue(body, SessionDto.class);
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getDeprecated()).isEqualTo(true);
            assertThat(result.getSelfAssessment()).isEqualTo(AssessmentType.poor);
            assertThat(result.getCurrentLineId()).isEqualTo(curLineId);
        } else {
            throw new Exception();
        }
    }

    @Test
    @Transactional
    @DisplayName("updating throws exception and returns 422")
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = { Role.verified})
    public void updateSessionThrowsUnprocessableEntityException() throws Exception {
        List<Line> lines = lineRepository.findByScriptId(1L);
        UpdateSessionDto updateSessionDto = new UpdateSessionDto();
        updateSessionDto.setCurrentLineId(lines.get(lines.size()-1).getId());

        mockMvc
            .perform(MockMvcRequestBuilders
                .patch(SessionEndpoint.path + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(updateSessionDto))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional
    @DisplayName("updating throws exception and returns 404")
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = { Role.verified})
    public void updateSessionThrowsNotFoundException() throws Exception {
        UpdateSessionDto updateSessionDto = new UpdateSessionDto();
        updateSessionDto.setCurrentLineId(-1L);

        mockMvc
            .perform(MockMvcRequestBuilders
                .patch(SessionEndpoint.path + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(updateSessionDto))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @DisplayName("returns finished session and 200")
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = { Role.verified})
    public void finishSessionCorrectly() throws Exception {
        byte[] body = mockMvc
            .perform(MockMvcRequestBuilders
                .post(SessionEndpoint.path + "/1")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsByteArray();

        SessionDto result = objectMapper.readValue(body, SessionDto.class);
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getEnd()).isNotNull();
    }

    @Test
    @Transactional
    @DisplayName("finish throws conflict exception and 409")
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = { Role.verified})
    public void finishSessionThrowsConflictException() throws Exception {
        Optional<Session> session = sessionRepository.findById(1L);
        if (session.isPresent()) {
            Session curSession = session.get();
            curSession.setDeprecated(true);
            sessionRepository.save(curSession);

            mockMvc
                .perform(MockMvcRequestBuilders
                    .post(SessionEndpoint.path + "/1")
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
        } else {
            throw new Exception();
        }
    }

    @Test
    @Transactional
    @DisplayName("returns found session and 200")
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = { Role.verified})
    public void findSessionByIdCorrectly() throws Exception {
        byte[] body = mockMvc
            .perform(MockMvcRequestBuilders
                .get(SessionEndpoint.path + "/1")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsByteArray();

        SessionDto result = objectMapper.readValue(body, SessionDto.class);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    @Transactional
    @DisplayName("session not found and 404")
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = { Role.verified})
    public void findSessionByIdThrowsNotFoundException() throws Exception {
        mockMvc
            .perform(MockMvcRequestBuilders
                .get(SessionEndpoint.path + "/-1")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @DisplayName("returns all sessions for user and 200")
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = { Role.verified})
    public void findAllSessionsCorrectly() throws Exception {
        byte[] body = mockMvc
            .perform(MockMvcRequestBuilders
                .get(SessionEndpoint.path)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsByteArray();

        List<SessionDto> sessions = objectMapper.readerFor(SessionDto.class).<SessionDto>readValues(body).readAll();
        assertThat(sessions).isNotNull();
        assertThat(sessions.size()).isEqualTo(12);
        assertThat(sessions.get(0).getId()).isEqualTo(1L);
        assertThat(sessions.get(1).getId()).isEqualTo(2L);
        assertThat(sessions.get(2).getId()).isEqualTo(3L);
    }
}
