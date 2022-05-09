package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Profile("datagen")
@Component
public class UserDataGenerator {

    private static final int NUMBER_OF_USERS_TO_GENERATE = 20;
    private static final String TEST_USER_FIRST_NAME = "firstName";
    private static final String TEST_USER_LAST_NAME = "lastName";
    private static final String TEST_USER_EMAIL_LOCAL = "test";
    private static final String TEST_USER_EMAIL_DOMAIN = "@test.com";
    private static final String TEST_USER_PASSWORD = "password";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDataGenerator(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    private void generateUser() {
        if (userRepository.findAll().size() > 0) {
            log.debug("users already generated");
        } else {
            log.debug("generating {} user entries", NUMBER_OF_USERS_TO_GENERATE);
            for (int i = 0; i < NUMBER_OF_USERS_TO_GENERATE; i++) {
                User user = User.builder().firstName(TEST_USER_FIRST_NAME + i)
                    .lastName(TEST_USER_LAST_NAME + i)
                    .email(TEST_USER_EMAIL_LOCAL + i + TEST_USER_EMAIL_DOMAIN)
                    .passwordHash(passwordEncoder.encode(TEST_USER_PASSWORD + i)).build();
                log.debug("saving user {}", user);
                userRepository.save(user);
            }
        }
    }
}
