package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.datagenerator.UserDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.entity.Script;
import at.ac.tuwien.sepm.groupphase.backend.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.repository.ScriptRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ScriptService;
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
    ScriptRepository scriptRepository;

    @Nested
    @DisplayName("deleteScript()")
    class DeleteScript {

        @Test
        @Transactional
        @DisplayName("is ok")
        @WithMockUser(username = UserDataGenerator.TEST_USER_EMAIL_LOCAL + "1" + UserDataGenerator.TEST_USER_EMAIL_DOMAIN, password = UserDataGenerator.TEST_USER_PASSWORD + "1", roles = { Role.user, Role.verified, Role.admin })
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
}
