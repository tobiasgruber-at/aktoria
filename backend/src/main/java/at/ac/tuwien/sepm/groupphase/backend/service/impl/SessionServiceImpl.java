package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SessionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleSessionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UpdateSessionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.SessionMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UnauthorizedException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.LineRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RoleRepository;
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
import java.util.Set;

/**
 * Specific implementation of SessionService.
 *
 * @author Marvin Flandorfer
 */
@Service
@Slf4j
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;
    private final SectionRepository sectionRepository;
    private final RoleRepository roleRepository;
    private final LineRepository lineRepository;
    private final AuthorizationService authorizationService;
    private final SessionMapper sessionMapper;

    public SessionServiceImpl(SessionRepository sessionRepository, SectionRepository sectionRepository,
                              RoleRepository roleRepository, LineRepository lineRepository,
                              AuthorizationService authorizationService, SessionMapper sessionMapper) {
        this.sessionRepository = sessionRepository;
        this.sectionRepository = sectionRepository;
        this.roleRepository = roleRepository;
        this.lineRepository = lineRepository;
        this.authorizationService = authorizationService;
        this.sessionMapper = sessionMapper;
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
            Double coverage = computeCoverage(curSession.getSection(), line.get());
            if (coverage == null) {
                throw new IllegalStateException();
            }
            curSession.setCoverage(coverage);
        }
        if (updateSessionDto.getSelfAssessment() != null) {
            curSession.setSelfAssessment(updateSessionDto.getSelfAssessment());
        }
        curSession = sessionRepository.save(curSession);
        return sessionMapper.sessionToSessionDto(curSession);
    }

    @Override
    public SessionDto finish(Long id) {
        return null;
    }

    @Override
    public SessionDto findById(Long id) {
        return null;
    }

    private Double computeCoverage(Section section, Line line) {
        Line startLine = section.getStartLine();
        Line endLine = section.getEndLine();
        List<Line> linesInBetween = lineRepository.findByStartLineAndEndLine(startLine, endLine);
        for (int i = 0; i < linesInBetween.size(); i++) {
            if (linesInBetween.get(i).getIndex().equals(line.getIndex())) {
                return (double) (i+1)/linesInBetween.size();
            }
        }
        return null;
    }
}
