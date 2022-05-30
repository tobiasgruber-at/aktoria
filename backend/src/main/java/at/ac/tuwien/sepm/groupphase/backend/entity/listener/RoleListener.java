package at.ac.tuwien.sepm.groupphase.backend.entity.listener;

import at.ac.tuwien.sepm.groupphase.backend.entity.Role;
import at.ac.tuwien.sepm.groupphase.backend.entity.Session;
import at.ac.tuwien.sepm.groupphase.backend.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PreRemove;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoleListener {

    private static SessionRepository sessionRepository;

    @Autowired
    private void init(SessionRepository sessionRepository) {
        RoleListener.sessionRepository = sessionRepository;
    }

    @PreRemove
    private void beforeRemove(Role role) {
        log.info("handle Sessions of role: " + role.getId());
        List<Session> sessions = role.getSessions().stream().toList();
        /* for (int i = 0; i < sessions.size(); i++) {
            sessions.get(i).setCurrentLine(null);
        }
        role.setSessions(null);
        for (int i = 0; i < sessions.size(); i++) {
            sessions.get(i).setRole(null);
        }*/
        sessionRepository.deleteAll(sessions);
    }
}
