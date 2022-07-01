package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SessionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleSessionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UpdateSessionDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Line;
import at.ac.tuwien.sepm.groupphase.backend.entity.Session;
import at.ac.tuwien.sepm.groupphase.backend.enums.AssessmentType;
import at.ac.tuwien.sepm.groupphase.backend.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.repository.LineRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SessionRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.SessionService;
import at.ac.tuwien.sepm.groupphase.backend.testhelpers.UserTestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Class for testing session-services.
 *
 * @author Marvin Flandorfer
 */

@SpringBootTest
@ActiveProfiles({"datagen", "test"})
public class SessionServiceUnitTest {

    @Autowired
    private SessionService sessionService;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private LineRepository lineRepository;

    @Test
    @DirtiesContext
    @DisplayName("saveSession() saves the session correctly")
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = {Role.verified})
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
        assertThat(result.getRole().getId()).isEqualTo(1L);
        assertThat(result.getCurrentLineIndex()).isNotNull();
    }

    @Test
    @DirtiesContext
    @DisplayName("updateSession() updates session correctly")
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = {Role.verified})
    public void updateSession() {
        Optional<Session> sessionOpt = sessionRepository.findById(1L);
        assertTrue(sessionOpt.isPresent());
        Long curLineId = sessionOpt.get().getCurrentLine().getId() + 1;
        UpdateSessionDto updateSessionDto = new UpdateSessionDto();
        updateSessionDto.setDeprecated(true);
        updateSessionDto.setSelfAssessment(AssessmentType.POOR);
        updateSessionDto.setCurrentLineId(curLineId);

        Optional<Line> lineOptional = lineRepository.findById(curLineId);
        assertTrue(lineOptional.isPresent());
        Long curLineIndex = lineOptional.get().getIndex();
        SessionDto result = sessionService.update(updateSessionDto, 1L);
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDeprecated()).isEqualTo(true);
        assertThat(result.getSelfAssessment()).isEqualTo(AssessmentType.POOR);
        assertThat(result.getCurrentLineIndex()).isEqualTo(curLineIndex);
    }

    @Test
    @DirtiesContext
    @DisplayName("finishSession() ends session correctly")
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = {Role.verified})
    public void finishSession() {
        SessionDto result = sessionService.finish(1L);
        assertThat(result.getEnd()).isNotNull();
    }

    @Test
    @DirtiesContext
    @DisplayName("findSessionById() finds session correctly")
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = {Role.verified})
    public void findSessionById() {
        SessionDto session = sessionService.findById(1L);
        assertThat(session).isNotNull();
        assertThat(session.getId()).isEqualTo(1L);
    }

    @Test
    @DirtiesContext
    @DisplayName("findPastSessions() finds all past user sessions correctly")
    @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = {Role.verified})
    public void findPastSessions() {
        List<SessionDto> sessions = sessionService.findQuerySessions(null, 1L, null).toList();
        assertThat(sessions).isNotNull();
        assertThat(sessions.size()).isEqualTo(2);
    }
}
