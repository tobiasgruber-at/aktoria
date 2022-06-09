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
import java.util.List;
import java.util.Random;
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
    Script (name = "Script <i>", owner = <i % [number_of_users]>)
    Page (script = <script_id>, index = <i>)
    Line (page = <page_id>, index = <i>, content = "Lorem ipsum dolor [...]", active = true)
    Role (script = <script_id>, name = "Role <i>", color = Color.CYAN)
     */

    public static final int NUMBER_OF_SCRIPTS_TO_GENERATE = 5;
    public static final int NUMBER_OF_PAGES_PER_SCRIPT = 10;
    public static final int NUMBER_OF_LINES_PER_PAGE = 10;
    public static final int NUMBER_OF_ROLES_PER_SCRIPT = 5;
    public static final int NUMBER_OF_ROLES_PER_LINE = 1;
    public static final String TEST_SCRIPT_NAME = "Script";
    //public static final String TEST_LINE_CONTENT = "Lorem ipsum dolor sit amet, "
    //    + "consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
    public static final String TEST_ROLE_NAME = "Role";
    public static final Color TEST_ROLE_COLOR = Color.CYAN;
    public static final String[] TEST_LINE_CONTENT = { "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
        "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit "
            + "esse cillum dolore eu fugiat nulla pariatur.",
        "Nisl suscipit adipiscing bibendum est ultricies integer quis. Libero enim sed faucibus turpis in eu. Dignissim enim sit amet venenatis urna. Vitae congue eu consequat ac felis donec et. "
            + "Pellentesque massa placerat duis ultricies. Ut diam quam nulla porttitor massa.",
        "Id nibh tortor id aliquet lectus. Posuere ac ut consequat semper viverra nam. Vitae nunc sed velit dignissim. Vel eros donec ac odio tempor orci.",
        "Zu Dionys dem Tirannen schlich Möros, den Dolch im Gewande, Ihn schlugen die Häscher in Bande. Was wolltest du mit dem Dolche, sprich! Entgegnet ihm finster der Wütherich. "
            + "„Die Stadt vom Tyrannen befreien!“ Das sollst du am Kreutze bereuen.",
        "Ich bin, spricht jener, zu sterben bereit, Und bitte nicht um mein Leben, Doch willst du Gnade mir geben, Ich flehe dich um drey Tage Zeit, Bis ich die Schwester dem Gatten gefreit, "
            + "Ich lasse den Freund dir als Bürgen, Ihn magst du, entrinn ich, erwürgen.",
        "Da lächelt der König mit arger List, Und spricht nach kurzem Bedenken: Drey Tage will ich dir schenken. Doch wisse! Wenn sie verstrichen die Frist, Eh du zurück mir gegeben bist, "
            + "So muß er statt deiner erblassen, Doch dir ist die Strafe erlassen.",
        "Wer reitet so spät durch Nacht und Wind? Es ist der Vater mit seinem Kind; Er hat den Knaben wohl in dem Arm, Er faßt ihn sicher, er hält ihn warm.",
        "\"Mein Sohn, was birgst du so bang dein Gesicht?\" \"Siehst, Vater, du den Erlkönig nicht? Den Erlenkönig mit Kron und Schweif?\" \"Mein Sohn, es ist ein Nebelstreif.\"",
        "\"Du liebes Kind, komm', geh' mit mir! Gar schöne Spiele spiel ich mit dir; Manch bunte Blumen sind an dem Strand; Meine Mutter hat manch gülden Gewand.\"",
        "\"Mein Vater, mein Vater, und hörest du nicht, Was Erlenkönig mir leise verspricht?\" \"Sei ruhig, bleibe ruhig, mein Kind! In dürren Blättern säuselt der Wind.\"",
        "Dieses Arzneimittel enthält 300 mg Fructose und 523 mg Sorbitol pro Lutschpastille. Sorbitol ist eine Quelle für Fructose.",
        "Sprechen Sie mit Ihrem Arzt bevor Sie (oder Ihr Kind) dieses Arzneimittel einnehmen/erhalten, wenn Ihr Arzt Ihnen mitgeteilt hat, dass Sie (oder Ihr Kind) eine Unverträglichkeit gegenüber einigen "
            + "Zuckern haben oder wenn bei Ihnen eine hereditäre Fructoseintoleranz (HFI) - eine seltene angeborene Erkrankung, bei der Fructose nicht abgebaut werden kann - festgestellt wurde.",
        "Die Anwendung bei Kindern unter 6 Jahren wird aufgrund fehlender Daten und der Gefahr von unabsichtlichem Verschlucken der ganzen Lutschpastille nicht empfohlen." };

    private final UserRepository userRepository;
    private final ScriptRepository scriptRepository;
    private final PageRepository pageRepository;
    private final LineRepository lineRepository;
    private final RoleRepository roleRepository;
    private long lineIndex = 1;

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
                    .owner(users.get((i - 1) % usersSize)).build();
                log.debug("saving script {}", script);
                scriptRepository.save(script);
                lineIndex = 1;
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
            Line line = Line.builder().page(page).index(lineIndex)
                .content(randomContentSelection()).active(true).build();
            lineIndex++;
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
            if (scripts.get(0).getRoles().stream().toList().get(0).getLines() == null) {
                log.debug("already generated spoken-by entries");
            } else {
                log.debug("generating spoken-by entries");
                for (Script script : scripts) {
                    List<Role> roles = script.getRoles().stream().toList();
                    List<Page> pages = script.getPages();
                    for (Page page : pages) {
                        List<Line> lines = page.getLines();
                        for (int i = 0; i < NUMBER_OF_ROLES_PER_LINE; i++) {
                            for (int j = 0; j < lines.size(); j++) {
                                Line line = lines.get(j);
                                Set<Role> spokenBy = line.getSpokenBy() == null ? new HashSet<>() : line.getSpokenBy();
                                spokenBy.add(roles.get((i + j) % roles.size()));
                                line.setSpokenBy(spokenBy);
                                log.debug("update line {}", line);
                                lineRepository.save(line);
                            }
                        }
                    }
                }
            }
        }
    }

    private String randomContentSelection() {
        Random r = new Random();
        return TEST_LINE_CONTENT[r.nextInt(TEST_LINE_CONTENT.length)];
    }
}
