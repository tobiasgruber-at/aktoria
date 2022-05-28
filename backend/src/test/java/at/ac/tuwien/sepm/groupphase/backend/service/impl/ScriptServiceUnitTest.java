package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.datagenerator.UserDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Script;
import at.ac.tuwien.sepm.groupphase.backend.entity.SecureToken;
import at.ac.tuwien.sepm.groupphase.backend.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.enums.TokenType;
import at.ac.tuwien.sepm.groupphase.backend.exception.UnauthorizedException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ScriptRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ScriptService;
import at.ac.tuwien.sepm.groupphase.backend.service.SecureTokenService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.testhelpers.UserTestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Class for testing script service.
 *
 * @author Marvin Flandorfer
 */
@ActiveProfiles({ "test", "datagen" })
@SpringBootTest
public class ScriptServiceUnitTest {

    @Autowired
    ScriptService scriptService;

    @Autowired
    SecureTokenService secureTokenService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    @Autowired
    ScriptRepository scriptRepository;

    @Autowired
    UserService userService;

    @Nested
    @DisplayName("deleteScript()")
    class DeleteScriptTest {

        @Test
        @Transactional
        @DisplayName("is ok")
        @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = { Role.user, Role.verified, Role.admin })
        public void deleteScriptIsOk() {
            Optional<Script> data = scriptRepository.findById(1L);
            assertTrue(data.isPresent());
            scriptService.delete(data.get().getId());
            data = scriptRepository.findById(1L);
            assertTrue(data.isEmpty());
        }

        @Test
        @Transactional
        @DisplayName("throws Exception")
        @WithMockUser(username = UserTestHelper.dummyUserEmail, password = UserTestHelper.dummyUserPassword, roles = { Role.user, Role.verified, Role.admin })
        public void deleteScriptThrowsException() {
            assertThrows(RuntimeException.class, () -> scriptService.delete(-200L));
        }
    }

    @Nested
    @DisplayName("addParticipant()")
    class AddingParticipantTest {

        @Test
        @Transactional
        @DisplayName("adding Participant works")
        @WithMockUser(username = UserDataGenerator.TEST_USER_EMAIL_LOCAL + "4" + UserDataGenerator.TEST_USER_EMAIL_DOMAIN, password = UserDataGenerator.TEST_USER_PASSWORD + "2", roles = { Role.user, Role.verified, Role.admin })
        public void addingParticipantWorks() {
            Optional<Script> scriptOptional = scriptRepository.findById(3L);
            assertTrue(scriptOptional.isPresent());
            Script script = scriptOptional.get();

            SecureToken token = secureTokenService.createSecureToken(TokenType.INVITE_PARTICIPANT, 15);
            token.setToken("ValidToken");
            token.setScript(script);
            secureTokenService.saveSecureToken(token);

            scriptService.addParticipant(3L, "ValidToken");

            assertTrue(userRepository.findById(4L).get().getParticipatesIn().contains(script));
        }

        @Test
        @Transactional
        @DisplayName("adding Participant throws UnauthorizedException")
        @WithMockUser(username = UserDataGenerator.TEST_USER_EMAIL_LOCAL + "4" + UserDataGenerator.TEST_USER_EMAIL_DOMAIN, password = UserDataGenerator.TEST_USER_PASSWORD + "2", roles = { Role.user, Role.verified, Role.admin })
        public void addingParticipantNotFound() {
            SecureToken token = secureTokenService.createSecureToken(TokenType.INVITE_PARTICIPANT, 15);
            token.setToken("InvalidToken");
            secureTokenService.saveSecureToken(token);

            assertThrows(UnauthorizedException.class, () -> scriptService.addParticipant(-3L, "InvalidToken"));
        }
    }
}
