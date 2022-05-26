package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Line;
import at.ac.tuwien.sepm.groupphase.backend.entity.Page;
import at.ac.tuwien.sepm.groupphase.backend.entity.Script;
import at.ac.tuwien.sepm.groupphase.backend.entity.Section;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.repository.LineRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ScriptRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectionRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Data generator for sections and sessions.
 *
 * @author Marvin Flandorfer
 */
@Slf4j
@Profile("datagen")
@Component
public class SectionDataGenerator {

    public static final int NUMBER_OF_SECTIONS_PER_USER_PER_SCRIPT = 3;
    public static final String TEST_SECTION_NAME = "Test Section";
    private final SectionRepository sectionRepository;
    private final UserRepository userRepository;
    private final ScriptRepository scriptRepository;
    private final LineRepository lineRepository;

    public SectionDataGenerator(SectionRepository sectionRepository, UserRepository userRepository, ScriptRepository scriptRepository, LineRepository lineRepository) {
        this.sectionRepository = sectionRepository;
        this.userRepository = userRepository;
        this.scriptRepository = scriptRepository;
        this.lineRepository = lineRepository;
    }

    @Transactional
    public void generateSection() {
        if (sectionRepository.findAll().size() > 0) {
            log.debug("sections already generated");
        } else {
            log.debug("generating {} section entries", NUMBER_OF_SECTIONS_PER_USER_PER_SCRIPT);
            List<Script> scripts = scriptRepository.findAll();
            for (Script script : scripts) {
                List<User> users = new LinkedList<>();
                users.add(script.getOwner());
                users.addAll(script.getParticipants());
                List<Page> pages = script.getPages();
                List<Line> lines = new LinkedList<>();
                for (Page page : pages) {
                    lines.addAll(page.getLines());
                }
                int numberOfLines = lines.size();
                for (User user : users) {
                    Section section = Section.builder()
                        .name(TEST_SECTION_NAME)
                        .owner(user)
                        .startLine(lines.get(randomizedLineSelect(0, numberOfLines / 2)))
                        .endLine(lines.get(randomizedLineSelect(numberOfLines / 2, numberOfLines)))
                        .build();
                    log.debug("saving section {}", section);
                    sectionRepository.save(section);
                }
            }
        }
    }

    private int randomizedLineSelect(int lowerBound, int upperBound) {
        Random r = new Random();
        int diff = upperBound - lowerBound;
        return r.nextInt(diff) + lowerBound;
    }
}
