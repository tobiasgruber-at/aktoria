package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Script;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.repository.ScriptRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Data generator for users.
 *
 * @author Marvin Flandorfer
 */
@Slf4j
@Profile("datagen")
@Component
public class UserDataGenerator {

    /*
    User test data is built in accord to the following schema:
    User (firstName = "testFirst<i>", lastName = "testLast<i>", email = "test<i>@test.com", password = "password<i>", verified = true)
     */

    public static final int NUMBER_OF_USERS_TO_GENERATE = 20;
    public static final int NUMBER_OF_PARTICIPANTS_PER_SCRIPT = 5;
    public static final String TEST_USER_PASSWORD = "password";
    public static final String TEST_USER_FIRST_NAME = "testFirst";
    public static final String TEST_USER_LAST_NAME = "testLast";
    public static final String TEST_USER_EMAIL_LOCAL = "test";
    public static final String TEST_USER_EMAIL_DOMAIN = "@test.com";
    private final UserRepository userRepository;
    private final ScriptRepository scriptRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDataGenerator(UserRepository userRepository, ScriptRepository scriptRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.scriptRepository = scriptRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void generateUser() {
        if (userRepository.findAll().size() > 0) {
            log.debug("users already generated");
        } else {
            log.debug("generating {} user entries", NUMBER_OF_USERS_TO_GENERATE);
            for (int i = 1; i <= NUMBER_OF_USERS_TO_GENERATE; i++) {
                User user = User.builder().firstName(TEST_USER_FIRST_NAME + i)
                    .lastName(TEST_USER_LAST_NAME + i)
                    .email(TEST_USER_EMAIL_LOCAL + i + TEST_USER_EMAIL_DOMAIN)
                    .passwordHash(passwordEncoder.encode(TEST_USER_PASSWORD + i))
                    .verified(true).build();
                log.debug("saving user {}", user);
                userRepository.save(user);
            }
        }
    }

    @Transactional
    public void generateParticipation() {
        List<Script> scripts = scriptRepository.findAll();
        List<User> users = userRepository.findAll();
        if (scripts.isEmpty() || users.isEmpty()) {
            log.debug("cannot generate participation entities");
        } else {
            if (scripts.get(0).getParticipants() == null) {
                log.debug("participation already generated");
            } else {
                log.debug("generating participation entries");
                int count = 0;
                for (Script script : scripts) {
                    Set<User> participants = new HashSet<>();
                    User owner = script.getOwner();
                    int ownerFlag = 0;
                    for (int i = 0; i < NUMBER_OF_PARTICIPANTS_PER_SCRIPT + ownerFlag; i++) {
                        User user = users.get(count % users.size());
                        if (owner.getId().equals(user.getId())) {
                            ownerFlag++;
                            count++;
                            continue;
                        }
                        participants.add(user);
                        count++;
                    }
                    log.debug("update script {}", script);
                    script.setParticipants(participants);
                    scriptRepository.save(script);
                }
            }
        }
    }
}
