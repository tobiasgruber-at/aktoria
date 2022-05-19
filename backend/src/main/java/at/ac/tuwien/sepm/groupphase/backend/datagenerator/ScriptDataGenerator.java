package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Line;
import at.ac.tuwien.sepm.groupphase.backend.entity.Page;
import at.ac.tuwien.sepm.groupphase.backend.entity.Role;
import at.ac.tuwien.sepm.groupphase.backend.entity.Script;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.repository.LineRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RoleRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ScriptRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.awt.Color;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Data generator for scripts (including pages, lines and rolls).
 *
 * @author Marvin Flandorfer
 */
@Slf4j
@Profile("datagen")
@Component
public class ScriptDataGenerator {

    /*
    Script test data is built in accord with the following schema:
    Script (name = "Script <i>", owner = <i % [number_of_users] + 1>)
    Page (script = <script_id>, index = <i>)
    Line (page = <page_id>, index = <i>, content = "Lorem ipsum dolor [...]", active = true)
    Role (script = <script_id>, name = "Role <i>", color = Color.CYAN)
     */

    private static final int NUMBER_OF_SCRIPTS_TO_GENERATE = 5;
    private static final int NUMBER_OF_PAGES_PER_SCRIPT = 10;
    private static final int NUMBER_OF_LINES_PER_PAGE = 10;
    private static final int NUMBER_OF_ROLES_PER_SCRIPT = 5;

    private static final String TEST_SCRIPT_NAME = "Script";
    private static final String TEST_LINE_CONTENT = "Lorem ipsum dolor sit amet, "
        + "consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
    private static final String TEST_ROLE_NAME = "Role";
    private static final Color TEST_ROLE_COLOR = Color.CYAN;

    private final UserRepository userRepository;
    private final ScriptRepository scriptRepository;
    private final PageRepository pageRepository;
    private final LineRepository lineRepository;
    private final RoleRepository roleRepository;

    public ScriptDataGenerator(UserRepository userRepository, ScriptRepository scriptRepository,
                               PageRepository pageRepository, LineRepository lineRepository,
                               RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.scriptRepository = scriptRepository;
        this.pageRepository = pageRepository;
        this.lineRepository = lineRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public void generateScript() {
        if (scriptRepository.findAll().size() > 0) {
            log.debug("scripts already generated");
        } else {
            log.debug("generating {} script entries", NUMBER_OF_SCRIPTS_TO_GENERATE);
            List<User> users = userRepository.findAll();
            int usersSize = users.size();
            for (int i = 1; i <= NUMBER_OF_SCRIPTS_TO_GENERATE; i++) {
                Script script = Script.builder().name(TEST_SCRIPT_NAME + " " + i)
                    .owner(users.get(i % usersSize)).build();
                log.debug("saving script {}", script);
                scriptRepository.save(script);
                generatePage(script);
                generateRole(script);
            }
        }
    }

    private void generatePage(Script script) {
        log.debug("generating {} page entries for script {}", NUMBER_OF_PAGES_PER_SCRIPT, script);
        for (int i = 1; i <= NUMBER_OF_PAGES_PER_SCRIPT; i++) {
            Page page = Page.builder().script(script).index((long) i).build();
            log.debug("saving page {}", page);
            pageRepository.save(page);
            generateLine(page);
        }
    }

    private void generateLine(Page page) {
        log.debug("generating {} line entries for page {}", NUMBER_OF_LINES_PER_PAGE, page);
        for (int i = 1; i <= NUMBER_OF_LINES_PER_PAGE; i++) {
            Line line = Line.builder().page(page).index((long) i)
                .content(TEST_LINE_CONTENT).active(true).build();
            log.debug("saving line {}", line);
            lineRepository.save(line);
        }
    }

    private void generateRole(Script script) {
        log.debug("generating {} role entries for script {}", NUMBER_OF_ROLES_PER_SCRIPT, script);
        for (int i = 0; i < NUMBER_OF_ROLES_PER_SCRIPT; i++) {
            Role role = Role.builder().script(script).name(TEST_ROLE_NAME + " " + i)
                .color(TEST_ROLE_COLOR).build();
            log.debug("saving role {}", role);
            roleRepository.save(role);
        }
    }

    @Transactional
    public void generateSpokenBy() {
        List<Script> scripts = scriptRepository.findAll();
        if (scripts.isEmpty()) {
            log.debug("cannot generate spoken-by entries without scripts");
        } else {
            log.debug("generating spoken-by entries");
            for (Script script : scripts) {
                List<Role> roles = script.getRoles().stream().toList();
                List<Page> pages = script.getPages();
                for (Page page : pages) {
                    List<Line> lines = page.getLines();
                    for (int i = 0; i < lines.size(); i++) {
                        Set<Role> spokenBy = new HashSet<>();
                        spokenBy.add(roles.get(i % roles.size()));
                        Line line = lines.get(i);
                        line.setSpokenBy(spokenBy);
                        log.debug("update line {}", line);
                        lineRepository.save(line);
                    }
                }
            }
        }
    }
}
