package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SessionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleSessionDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.exception.UnauthorizedException;
import at.ac.tuwien.sepm.groupphase.backend.repository.RoleRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectionRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SessionRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.AuthorizationService;
import at.ac.tuwien.sepm.groupphase.backend.service.SessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Specific implementation of SessionService
 *
 * @author Marvin Flandorfer
 */
@Service
@Slf4j
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;
    private final SectionRepository sectionRepository;
    private final RoleRepository roleRepository;
    private final AuthorizationService authorizationService;

    public SessionServiceImpl(SessionRepository sessionRepository, SectionRepository sectionRepository, RoleRepository roleRepository, AuthorizationService authorizationService) {
        this.sessionRepository = sessionRepository;
        this.sectionRepository = sectionRepository;
        this.roleRepository = roleRepository;
        this.authorizationService = authorizationService;
    }

    @Override
    @Transactional
    public SessionDto save(SimpleSessionDto simpleSessionDto) {
        log.trace("save(sessionDto = {}", simpleSessionDto);
        User user = authorizationService.getLoggedInUser();
        if(user == null){
            throw new UnauthorizedException();
        }

        return null;
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
