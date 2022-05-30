package at.ac.tuwien.sepm.groupphase.backend.entity.listener;

import at.ac.tuwien.sepm.groupphase.backend.entity.Line;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PreRemove;

@Slf4j
@Component
@RequiredArgsConstructor
public class LineListener {

    private static UserRepository userRepository;

    @Autowired
    private void init(UserRepository userRepository) {
        LineListener.userRepository = userRepository;
    }

    @PreRemove
    private void beforeRemove(Line line) {
        log.info("handle remove for line: " + line.getId());
        line.setRecordedBy(null);
    }
}
