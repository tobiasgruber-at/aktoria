package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SessionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleSessionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UpdateSessionDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Session;
import at.ac.tuwien.sepm.groupphase.backend.enums.AssessmentType;
import at.ac.tuwien.sepm.groupphase.backend.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.repository.SessionRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.SessionService;
import at.ac.tuwien.sepm.groupphase.backend.testhelpers.UserTestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles({ "datagen", "test" })
public class SessionServiceUnitTest {

    @Autowired
    private SessionService sessionService;
    @Autowired
    private SessionRepository sessionRepository;

    @Test
    @DisplayName("saveSession() saves the session correctly")
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = { Role.verified })
    public void saveSession() {
        SimpleSessionDto simpleSessionDto = new SimpleSessionDto();
        simpleSessionDto.setSectionId(1L);
        simpleSessionDto.setRoleId(1L);

        SessionDto result = sessionService.save(simpleSessionDto);
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
    @DisplayName("updateSession() updates session correctly")
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = { Role.verified })
    public void updateSession() throws Exception {
        Optional<Session> sessionOpt = sessionRepository.findById(1L);
        if (sessionOpt.isPresent()) {
            Long curLineId = sessionOpt.get().getCurrentLine().getId() + 1;
            UpdateSessionDto updateSessionDto = new UpdateSessionDto();
            updateSessionDto.setDeprecated(true);
            updateSessionDto.setSelfAssessment(AssessmentType.poor);
            updateSessionDto.setCurrentLineId(curLineId);

            SessionDto result = sessionService.update(updateSessionDto, 1L);
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getDeprecated()).isEqualTo(true);
            assertThat(result.getSelfAssessment()).isEqualTo(AssessmentType.poor);
            assertThat(result.getCurrentLineId()).isEqualTo(curLineId);
        } else {
            throw new Exception();
        }
    }

    @Test
    @DisplayName("finishSession() ends session correctly")
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = { Role.verified })
    public void finishSession() {
        SessionDto result = sessionService.finish(1L);
        assertThat(result.getEnd()).isNotNull();
    }

    @Test
    @DisplayName("findSessionById() finds session correctly")
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = { Role.verified })
    public void findSessionById() {
        SessionDto session = sessionService.findById(1L);
        assertThat(session).isNotNull();
        assertThat(session.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("findAllSessions() finds all user sessions correctly")
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = { Role.verified })
    public void findAllSessions() {
        List<SessionDto> sessions = sessionService.findAll().toList();
        assertThat(sessions).isNotNull();
        assertThat(sessions.size()).isEqualTo(12);
        assertThat(sessions.get(0).getId()).isEqualTo(1L);
        assertThat(sessions.get(1).getId()).isEqualTo(2L);
        assertThat(sessions.get(2).getId()).isEqualTo(3L);
    }
}
