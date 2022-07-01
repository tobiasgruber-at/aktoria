package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.datagenerator.UserDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.entity.Section;
import at.ac.tuwien.sepm.groupphase.backend.entity.Session;
import at.ac.tuwien.sepm.groupphase.backend.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.exception.UnauthorizedException;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectionRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.SessionService;
import at.ac.tuwien.sepm.groupphase.backend.testhelpers.UserTestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Class for testing session-services.
 *
 * @author Simon Josef Kreuzpointner
 */

@ActiveProfiles({"test", "datagen"})
@SpringBootTest
class SessionServiceImplIntegrationTest {

    @Autowired
    private SessionService sessionService;
    @Autowired
    private SectionRepository sectionRepository;

    @Nested
    @DisplayName("deprecateAffected()")
    class DeprecateAffected {

        @Nested
        @DisplayName("works correctly")
        class WorksCorrectly {

            @Test
            @Transactional
            @DisplayName("for owner")
            @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = {Role.verified})
            void deprecateAffectedWorksForOwner() {
                final Long scriptId = 1L;
                final Long userId = 1L;

                sessionService.deprecateAffected(scriptId);
                List<Section> sectionList = sectionRepository.findAll();
                List<Section> affectedSections = new LinkedList<>();
                for (Section s : sectionList) {
                    if (s.getStartLine().getPage().getScript().getId().equals(scriptId)
                        && s.getOwner().getId().equals(userId)) {
                        affectedSections.add(s);
                    }
                }
                List<Session> affectedSessions = new LinkedList<>();
                for (Section s : affectedSections) {
                    affectedSessions.addAll(s.getSessions());
                }
                for (Session s : affectedSessions) {
                    assertTrue(s.getDeprecated());
                }
            }

            @Test
            @Transactional
            @DisplayName("for participant")
            @WithMockUser(username = UserDataGenerator.TEST_USER_EMAIL_LOCAL + "2" + UserDataGenerator.TEST_USER_EMAIL_DOMAIN, password = UserDataGenerator.TEST_USER_PASSWORD + "2", roles = {Role.verified})
            void deprecateAffectedWorksForParticipant() {
                final Long scriptId = 1L;
                final Long userId = 2L;

                sessionService.deprecateAffected(scriptId);
                List<Section> sectionList = sectionRepository.findAll();
                List<Section> affectedSections = new LinkedList<>();
                for (Section s : sectionList) {
                    if (s.getStartLine().getPage().getScript().getId().equals(scriptId)
                        && s.getOwner().getId().equals(userId)) {
                        affectedSections.add(s);
                    }
                }
                List<Session> affectedSessions = new LinkedList<>();
                for (Section s : affectedSections) {
                    affectedSessions.addAll(s.getSessions());
                }
                for (Session s : affectedSessions) {
                    assertTrue(s.getDeprecated());
                }
            }
        }

        @Nested
        @DisplayName("throws")
        class Throws {

            @Test
            @Transactional
            @DisplayName("UnauthorizedException")
            @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = {Role.verified})
            void deprecateAffectedUnauthorized() {
                assertThrows(UnauthorizedException.class, () -> sessionService.deprecateAffected(2L));
            }
        }
    }
}