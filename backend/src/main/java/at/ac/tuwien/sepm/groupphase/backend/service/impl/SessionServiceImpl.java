package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SessionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleSessionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UpdateSessionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.SessionMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Line;
import at.ac.tuwien.sepm.groupphase.backend.entity.Role;
import at.ac.tuwien.sepm.groupphase.backend.entity.Script;
import at.ac.tuwien.sepm.groupphase.backend.entity.Section;
import at.ac.tuwien.sepm.groupphase.backend.entity.Session;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UnauthorizedException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.LineRepository;
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
import java.util.stream.Stream;

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
    private final LineRepository lineRepository;
    private final SessionMapper sessionMapper;

    public SessionServiceImpl(AuthorizationService authorizationService,
                              SessionRepository sessionRepository,
                              ScriptRepository scriptRepository,
                              SectionRepository sectionRepository,
                              RoleRepository roleRepository,
                              LineRepository lineRepository,
                              SessionMapper sessionMapper) {
        this.authorizationService = authorizationService;
        this.sessionRepository = sessionRepository;
        this.scriptRepository = scriptRepository;
        this.sectionRepository = sectionRepository;
        this.roleRepository = roleRepository;
        this.lineRepository = lineRepository;
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
    @Transactional
    public SessionDto update(UpdateSessionDto updateSessionDto, Long id) {
        log.trace("update(session = {}, {})", id, updateSessionDto);
        User user = authorizationService.getLoggedInUser();
        if (user == null) {
            throw new UnauthorizedException();
        }
        Optional<Session> session = sessionRepository.findById(id);
        if (session.isEmpty()) {
            throw new NotFoundException("Session not found");
        }
        Session curSession = session.get();
        if (!curSession.getSection().getOwner().getId().equals(user.getId())) {
            throw new UnauthorizedException("User not permitted to update this session");
        }
        if (updateSessionDto.getDeprecated() != null) {
            curSession.setDeprecated(updateSessionDto.getDeprecated());
        }
        if (updateSessionDto.getCurrentLineId() != null) {
            Optional<Line> line = lineRepository.findById(updateSessionDto.getCurrentLineId());
            if (line.isEmpty()) {
                throw new NotFoundException("Current line not found");
            }
            if (!curSession.getSection().getStartLine().getPage().getScript().getId()
                .equals(line.get().getPage().getScript().getId())) {
                throw new ValidationException("Current line not in correct script");
            }
            if (curSession.getSection().getEndLine().getPage().getIndex() <= line.get().getPage().getIndex()) {
                if (curSession.getSection().getEndLine().getPage().getIndex() < line.get().getPage().getIndex()
                    || curSession.getSection().getEndLine().getIndex() < line.get().getIndex()) {
                    throw new ValidationException("Current line may not be after the end of the section");
                }
            }
            curSession.setCurrentLine(line.get());
        }
        if (updateSessionDto.getSelfAssessment() != null) {
            curSession.setSelfAssessment(updateSessionDto.getSelfAssessment());
        }
        curSession = sessionRepository.save(curSession);
        return sessionMapper.sessionToSessionDto(curSession);
    }

    @Override
    @Transactional
    public SessionDto finish(Long id) {
        log.trace("finish(id = {})", id);
        User user = authorizationService.getLoggedInUser();
        if (user == null) {
            throw new UnauthorizedException();
        }
        Optional<Session> session = sessionRepository.findById(id);
        if (session.isEmpty()) {
            throw new NotFoundException("Session not found");
        }
        Session curSession = session.get();
        if (!curSession.getSection().getOwner().getId().equals(user.getId())) {
            throw new UnauthorizedException("User not permitted to update this session");
        }
        if (curSession.getDeprecated()) {
            throw new ConflictException("Session is already deprecated");
        }
        curSession.setEnd(LocalDateTime.now());
        curSession = sessionRepository.save(curSession);
        return sessionMapper.sessionToSessionDto(curSession);
    }

    @Override
    @Transactional
    public SessionDto findById(Long id) {
        log.trace("findSessionById(id = {})", id);
        User user = authorizationService.getLoggedInUser();
        if (user == null) {
            throw new UnauthorizedException();
        }
        Optional<Session> session = sessionRepository.findById(id);
        if (session.isEmpty()) {
            throw new NotFoundException("Session not found");
        }
        Session curSession = session.get();
        if (!curSession.getSection().getOwner().getId().equals(user.getId())) {
            throw new UnauthorizedException("User not permitted to view this session");
        }
        return sessionMapper.sessionToSessionDto(curSession);
    }

    @Override
    @Transactional
    public Stream<SessionDto> findQuerySessions(Boolean deprecated, Long sectionId, Boolean past) {
        log.trace("findPastSessions(deprecated = {}, sectionId = {})", deprecated, sectionId);
        User user = authorizationService.getLoggedInUser();
        if (user == null) {
            throw new UnauthorizedException();
        }
        List<Session> sessions;
        if (past == null || !past) {
            if (deprecated == null || !deprecated) {
                if (sectionId == null) {
                    sessions = sessionRepository.findByDeprecatedAndUser(false, user);
                } else {
                    sessions = sessionRepository.findBySectionAndDeprecatedAndUser(sectionId, false, user);
                }
            } else {
                if (sectionId == null) {
                    sessions = sessionRepository.findByDeprecatedAndUser(true, user);
                } else {
                    sessions = sessionRepository.findBySectionAndDeprecatedAndUser(sectionId, true, user);
                }
            }
        } else {
            if (deprecated == null || !deprecated) {
                if (sectionId == null) {
                    sessions = sessionRepository.findByDeprecatedAndUserAndPast(false, user);
                } else {
                    sessions = sessionRepository.findBySectionAndDeprecatedAndUserAndPast(sectionId, false, user);
                }
            } else {
                if (sectionId == null) {
                    sessions = sessionRepository.findByDeprecatedAndUserAndPast(true, user);
                } else {
                    sessions = sessionRepository.findBySectionAndDeprecatedAndUserAndPast(sectionId, true, user);
                }
            }
        }
        return sessions.stream().map(sessionMapper::sessionToSessionDto);
    }

    private Double computeCoverage(Section section, Line line) {
        log.trace("computeCoverage(section = {}, line = {})", section, line);
        Line startLine = section.getStartLine();
        Line endLine = section.getEndLine();
        List<Line> linesInBetween = lineRepository.findByStartLineAndEndLine(startLine, endLine);
        for (int i = 0; i < linesInBetween.size(); i++) {
            if (linesInBetween.get(i).getIndex().equals(line.getIndex())) {
                return (double) (i + 1) / linesInBetween.size();
            }
        }
        return null;
    }
}
