package at.ac.tuwien.sepm.groupphase.backend.entity.listener;

import at.ac.tuwien.sepm.groupphase.backend.entity.Session;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PreRemove;

@Slf4j
@Component
@RequiredArgsConstructor
public class SessionListener {

    private static SectionRepository sectionRepository;

    @Autowired
    private void init(SectionRepository sectionRepository) {
        SessionListener.sectionRepository = sectionRepository;
    }

    @PreRemove
    private void beforeRemove(Session session) {
        log.info("handle remove for session: " + session.getId());
    }
}
