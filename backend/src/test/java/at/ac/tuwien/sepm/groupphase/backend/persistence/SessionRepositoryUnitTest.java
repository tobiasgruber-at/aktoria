package at.ac.tuwien.sepm.groupphase.backend.persistence;

import at.ac.tuwien.sepm.groupphase.backend.entity.Session;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.repository.SessionRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles({ "test", "datagen" })
public class SessionRepositoryUnitTest {

    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    @DisplayName("find all sessions for a user correctly")
    public void findAllByUserCorrectly() {
        Set<Session> expected = new HashSet<>();
        List<Session> sessions = sessionRepository.findAll();
        User user = userRepository.getById(1L);
        for (Session session : sessions) {
            if (session.getSection().getOwner().getId().equals(user.getId())) {
                expected.add(session);
            }
        }
        Set<Session> actual = new HashSet<>(sessionRepository.findAllByUser(user));
        assertThat(actual).isEqualTo(expected);
    }
}
