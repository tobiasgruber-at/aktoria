package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SessionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleSessionDto;
import at.ac.tuwien.sepm.groupphase.backend.repository.SessionRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.SessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Specific implementation of SessionService
 *
 * @author Marvin Flandorfer
 */
@Service
@Slf4j
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;

    public SessionServiceImpl(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public SessionDto save(SimpleSessionDto simpleSessionDto) {
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
