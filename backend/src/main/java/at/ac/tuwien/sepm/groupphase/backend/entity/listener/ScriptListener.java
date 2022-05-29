package at.ac.tuwien.sepm.groupphase.backend.entity.listener;

import at.ac.tuwien.sepm.groupphase.backend.entity.Script;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PreRemove;

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
        log.info("delete owner relation for script: " + script.getId());
        script.getOwner().getScripts().remove(script);
        userRepository.save(script.getOwner());
    }
}
