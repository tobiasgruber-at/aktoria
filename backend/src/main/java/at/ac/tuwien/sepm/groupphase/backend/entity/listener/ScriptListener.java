package at.ac.tuwien.sepm.groupphase.backend.entity.listener;

import at.ac.tuwien.sepm.groupphase.backend.entity.Script;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PreRemove;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScriptListener {

    private static UserRepository userRepository;

    @Autowired
    private void init(UserRepository userRepository) {
        ScriptListener.userRepository = userRepository;
    }

    @PreRemove
    private void beforeRemove(Script script) {
        log.info("delete relations for script: " + script.getId());
        List<User> participants = script.getParticipants().stream().toList();
        for (int i = 0; i < participants.size(); i++) {
            User user = participants.get(i);
            user.getParticipatesIn().remove(script);
            userRepository.save(user);
        }
    }
}
