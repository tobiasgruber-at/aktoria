package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Line;
import at.ac.tuwien.sepm.groupphase.backend.entity.Page;
import at.ac.tuwien.sepm.groupphase.backend.entity.Role;
import at.ac.tuwien.sepm.groupphase.backend.entity.Script;
import at.ac.tuwien.sepm.groupphase.backend.entity.Section;
import at.ac.tuwien.sepm.groupphase.backend.entity.Session;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.enums.AssessmentType;
import at.ac.tuwien.sepm.groupphase.backend.repository.ScriptRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectionRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SessionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
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
    public static final int NUMBER_OF_UNFINISHED_SESSIONS_PER_SECTION = 1;
    public static final int NUMBER_OF_FINISHED_SESSION_PER_SECTION = 1;
    public static final String TEST_SECTION_NAME = "Test Section";
    private final SectionRepository sectionRepository;
    private final SessionRepository sessionRepository;
    private final ScriptRepository scriptRepository;

    public SectionDataGenerator(SectionRepository sectionRepository, SessionRepository sessionRepository, ScriptRepository scriptRepository) {
        this.sectionRepository = sectionRepository;
        this.sessionRepository = sessionRepository;
        this.scriptRepository = scriptRepository;
    }

    @Transactional
    public void generateSection() {
        if (sectionRepository.findAll().size() > 0) {
            log.debug("sections already generated");
        } else {
            log.debug("generating section entries");
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
                    for (int i = 0; i < NUMBER_OF_SECTIONS_PER_USER_PER_SCRIPT; i++) {
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
    }

    @Transactional
    public void generateSession() {
        if (sessionRepository.findAll().size() > 0) {
            log.debug("sessions already generated");
        } else {
            log.debug("generating session entries");
            List<Section> sections = sectionRepository.findAll();
            for (int i = 0; i < sections.size(); i++) {
                Section section = sections.get(i);
                List<Role> roles = section.getStartLine().getPage().getScript().getRoles().stream().toList();
                for (int j = 0; j < NUMBER_OF_UNFINISHED_SESSIONS_PER_SECTION; j++) {
                    Session session = Session.builder()
                        .section(section)
                        .currentLine(section.getStartLine())
                        .role(roles.get((i + j) % roles.size()))
                        .start(LocalDateTime.now())
                        .deprecated(false)
                        .build();
                    log.debug("save session {}", session);
                    sessionRepository.save(session);
                }
                for (int j = 0; j < NUMBER_OF_FINISHED_SESSION_PER_SECTION; j++) {
                    Session session = Session.builder()
                        .section(section)
                        .currentLine(section.getEndLine())
                        .role(roles.get((i + j) % roles.size()))
                        .start(LocalDateTime.now())
                        .end(LocalDateTime.now())
                        .coverage(1.0)
                        .selfAssessment(AssessmentType.good)
                        .deprecated(false)
                        .build();
                    log.debug("save session {}", session);
                    sessionRepository.save(session);
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
