package at.ac.tuwien.sepm.groupphase.backend.entity.listener;

import at.ac.tuwien.sepm.groupphase.backend.entity.Role;
import at.ac.tuwien.sepm.groupphase.backend.entity.Script;
import at.ac.tuwien.sepm.groupphase.backend.entity.Section;
import at.ac.tuwien.sepm.groupphase.backend.entity.Session;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.repository.RoleRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PreRemove;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScriptListener {

    private static UserRepository userRepository;
    private static RoleRepository roleRepository;

    @Autowired
    private void init(UserRepository userRepository, RoleRepository roleRepository) {
        ScriptListener.userRepository = userRepository;
        ScriptListener.roleRepository = roleRepository;
    }

    @PreRemove
    private void beforeRemove(Script script) {
        log.info("delete relations for script: " + script.getId());
        //remove partipates realtionship
        List<User> participants = script.getParticipants().stream().toList();
        for (int i = 0; i < participants.size(); i++) {
            User user = participants.get(i);
            user.getParticipatesIn().remove(script);
            userRepository.save(user);
        }

        //remove
        List<Role> roles = script.getRoles().stream().toList();
        if (roles.size() > 0) {
            Set<Session> sessions = roles.get(0).getSessions();
            for (int i = 1; i < roles.size(); i++) {
                sessions.addAll(roles.get(i).getSessions());
            }
            List<Session> sessionList = sessions.stream().toList();
            List<Section> sections = new LinkedList<>();
            for (int i = 0; i < sessionList.size(); i++) {
                sections.add(sessionList.get(i).getSection());
            }

            //then remove roles
            roleRepository.deleteAll(roles);
        }
    }
}
