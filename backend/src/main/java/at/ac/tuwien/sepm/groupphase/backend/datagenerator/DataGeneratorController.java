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
    private final SectionDataGenerator sectionDataGenerator;


    public DataGeneratorController(UserDataGenerator userDataGenerator, ScriptDataGenerator scriptDataGenerator, SectionDataGenerator sectionDataGenerator) {
        this.userDataGenerator = userDataGenerator;
        this.scriptDataGenerator = scriptDataGenerator;
        this.sectionDataGenerator = sectionDataGenerator;
    }

    @PostConstruct
    private void generateData() {
        userDataGenerator.generateUser();
        scriptDataGenerator.generateScript();
        scriptDataGenerator.generateSpokenBy();
        userDataGenerator.generateParticipation();
        sectionDataGenerator.generateSection();
        sectionDataGenerator.generateSession();
    }
}
