package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    User (firstName = "testFirst<i>", lastName = "testLast<i>", email = "test<i>@test.com", password = "password<i>", verified = false)
     */

    private static final int NUMBER_OF_USERS_TO_GENERATE = 20;
    private static final String TEST_USER_FIRST_NAME = "testFirst";
    private static final String TEST_USER_LAST_NAME = "testLast";
    private static final String TEST_USER_EMAIL_LOCAL = "test";
    private static final String TEST_USER_EMAIL_DOMAIN = "@test.com";
    private static final String TEST_USER_PASSWORD = "password";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDataGenerator(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
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
                    .verified(false).build();
                log.debug("saving user {}", user);
                userRepository.save(user);
            }
        }
    }
}
