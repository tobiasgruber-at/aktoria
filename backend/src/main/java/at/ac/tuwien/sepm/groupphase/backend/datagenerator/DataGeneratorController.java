package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Data generator controller.
 *
 * @author Marvin Flandorfer
 */
@Slf4j
@Profile("datagen")
@Component
public class DataGeneratorController {

    private final UserDataGenerator userDataGenerator;
    private final ScriptDataGenerator scriptDataGenerator;


    public DataGeneratorController(UserDataGenerator userDataGenerator, ScriptDataGenerator scriptDataGenerator) {
        this.userDataGenerator = userDataGenerator;
        this.scriptDataGenerator = scriptDataGenerator;
    }

    @PostConstruct
    private void generateData() {
        userDataGenerator.generateUser();
        scriptDataGenerator.generateScript();
    }
}
