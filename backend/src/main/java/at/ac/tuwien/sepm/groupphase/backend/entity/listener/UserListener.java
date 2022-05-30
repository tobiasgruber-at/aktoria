package at.ac.tuwien.sepm.groupphase.backend.entity.listener;

import at.ac.tuwien.sepm.groupphase.backend.entity.Line;
import at.ac.tuwien.sepm.groupphase.backend.entity.Script;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.repository.LineRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ScriptRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PreRemove;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserListener {

    private static ScriptRepository scriptRepository;
    private static LineRepository lineRepository;

    @Autowired
    private void init(ScriptRepository scriptRepository, LineRepository lineRepository) {
        UserListener.scriptRepository = scriptRepository;
        UserListener.lineRepository = lineRepository;
    }

    @PreRemove
    private void beforeRemove(User user) {
        log.info("delete line and script relationships for user: " + user.getId());
        List<Script> participates = user.getParticipatesIn().stream().toList();
        for (int i = 0; i < participates.size(); i++) {
            Script script = participates.get(i);
            script.getParticipants().remove(user);
            scriptRepository.save(script);
        }

        List<Line> lines = user.getLinesRecorded().stream().toList();
        for (int i = 0; i < lines.size(); i++) {
            Line line = lines.get(i);
            line.setRecordedBy(null);
            lineRepository.save(line);
        }

    }
}
