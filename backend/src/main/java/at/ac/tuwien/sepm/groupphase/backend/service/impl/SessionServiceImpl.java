package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SessionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleSessionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.SessionMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Role;
import at.ac.tuwien.sepm.groupphase.backend.entity.Script;
import at.ac.tuwien.sepm.groupphase.backend.entity.Section;
import at.ac.tuwien.sepm.groupphase.backend.entity.Session;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UnauthorizedException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.RoleRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ScriptRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectionRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SessionRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.AuthorizationService;
import at.ac.tuwien.sepm.groupphase.backend.service.SessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * A specific implementation of SessionService.
 *
 * @author Simon Josef Kreuzpointner
 * @author Marvin Flandorfer
 */

@Service
@Slf4j
public class SessionServiceImpl implements SessionService {
    private final AuthorizationService authorizationService;
    private final SessionRepository sessionRepository;
    private final ScriptRepository scriptRepository;
    private final SectionRepository sectionRepository;
    private final RoleRepository roleRepository;
    private final SessionMapper sessionMapper;

    public SessionServiceImpl(AuthorizationService authorizationService, SessionRepository sessionRepository, ScriptRepository scriptRepository, SectionRepository sectionRepository, RoleRepository roleRepository,
                              SessionMapper sessionMapper) {
        this.authorizationService = authorizationService;
        this.sessionRepository = sessionRepository;
        this.scriptRepository = scriptRepository;
        this.sectionRepository = sectionRepository;
        this.roleRepository = roleRepository;
        this.sessionMapper = sessionMapper;
    }

    @Transactional
    @Override
    public void deprecateAffected(Long id) {
        log.trace("deprecateAffected(id = {})", id);

        User user = authorizationService.getLoggedInUser();
        if (user == null) {
            throw new UnauthorizedException();
        }
        Optional<Script> scriptOptional = scriptRepository.findById(id);
        Script script;
        if (scriptOptional.isPresent()) {
            script = scriptOptional.get();
        } else {
            throw new NotFoundException();
        }
        if (!script.getOwner().getId().equals(user.getId())
            && script.getParticipants().stream().noneMatch(participant -> participant.getId().equals(user.getId()))) {
            throw new UnauthorizedException();
        }
        List<Section> sectionList = sectionRepository.findAll();
        List<Section> affected = new LinkedList<>();
        for (Section s : sectionList) {
            if (s.getStartLine().getPage().getScript().getId().equals(id)
                && s.getOwner().getId().equals(user.getId())) {
                affected.add(s);
            }
        }
        for (Section s : affected) {
            s.getSessions().forEach(session -> deprecate(session.getId()));
        }
    }

    @Transactional
    @Override
    public void deprecate(Long id) {
        log.trace("deprecate(id = {})", id);

        User user = authorizationService.getLoggedInUser();
        if (user == null) {
            throw new UnauthorizedException();
        }
        Optional<Session> sessionOptional = sessionRepository.findById(id);
        Session session;
        if (sessionOptional.isPresent()) {
            session = sessionOptional.get();
        } else {
            throw new NotFoundException();
        }
        Section section = session.getSection();
        if (!section.getOwner().getId().equals(user.getId())) {
            throw new UnauthorizedException();
        }
        session.setDeprecated(true);
        sessionRepository.save(session);
    }

    @Override
    @Transactional
    public SessionDto save(SimpleSessionDto simpleSessionDto) {
        log.trace("save(sessionDto = {}", simpleSessionDto);
        User user = authorizationService.getLoggedInUser();
        if (user == null) {
            throw new UnauthorizedException();
        }
        Optional<Section> section = sectionRepository.findById(simpleSessionDto.getSectionId());
        if (section.isEmpty()) {
            throw new ValidationException("Section not found");
        }
        if (!section.get().getOwner().getId().equals(user.getId())) {
            throw new UnauthorizedException("User not permitted to create session for this section");
        }
        Optional<Role> role = roleRepository.findById(simpleSessionDto.getRoleId());
        if (role.isEmpty()) {
            throw new ValidationException("Role not found");
        }
        Session session = Session.builder()
            .start(LocalDateTime.now())
            .section(section.get())
            .role(role.get())
            .currentLine(section.get().getStartLine())
            .coverage(0.0)
            .deprecated(false)
            .build();
        session = sessionRepository.save(session);
        return sessionMapper.sessionToSessionDto(session);
    }

    @Override
    public SessionDto update(SessionDto sessionDto, Long id) {
        return null;
    }

    @Override
    public SessionDto finish(Long id) {
        return null;
    }

    @Override
    public SessionDto findById(Long id) {
        return null;
    }
}
