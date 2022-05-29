package at.ac.tuwien.sepm.groupphase.backend.persistence;

import at.ac.tuwien.sepm.groupphase.backend.entity.Session;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.repository.SessionRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles({ "test", "datagen" })
public class SessionRepositoryUnitTest {

    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DirtiesContext
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

        List<Long> expectedIds = expected.stream().map(Session::getId).toList();
        List<Long> actualIds = actual.stream().map(Session::getId).toList();

        assertEquals(expectedIds.size(), actualIds.size());
        assertTrue(expectedIds.containsAll(actualIds));
    }
}
