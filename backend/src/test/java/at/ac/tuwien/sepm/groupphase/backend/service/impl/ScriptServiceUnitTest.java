package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Script;
import at.ac.tuwien.sepm.groupphase.backend.repository.ScriptRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ScriptService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    @Transactional
    @Disabled
    public void deleteScriptIsOk() {
        List<Script> data = scriptRepository.findAll();
        for (Script script : data) {
            scriptService.delete(script.getId());
        }
        assertEquals(0, scriptRepository.findAll().size());
    }

    @Test
    @Transactional
    public void deleteScriptThrowsException() {
        assertThrows(Exception.class, () -> scriptService.delete(-200L));
    }
}
