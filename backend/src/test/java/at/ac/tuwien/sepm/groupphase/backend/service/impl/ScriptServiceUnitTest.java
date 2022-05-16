package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.datagenerator.ScriptDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.entity.Script;
import at.ac.tuwien.sepm.groupphase.backend.repository.ScriptRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ScriptService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Class for testing script service.
 *
 * @author Marvin Flandorfer
 */
@ActiveProfiles({"test", "datagen"})
@SpringBootTest
public class ScriptServiceUnitTest {

    @Autowired
    ScriptService scriptService;

    @Autowired
    ScriptRepository scriptRepository;

    @Test
    @Transactional
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
