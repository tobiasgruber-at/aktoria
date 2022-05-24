package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LineDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UpdateLineDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.LineMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Line;
import at.ac.tuwien.sepm.groupphase.backend.entity.Script;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UnauthorizedException;
import at.ac.tuwien.sepm.groupphase.backend.repository.LineRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ScriptRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.AuthorizationService;
import at.ac.tuwien.sepm.groupphase.backend.service.LineService;
import at.ac.tuwien.sepm.groupphase.backend.validation.LineValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * A specific implementation of LineService.
 *
 * @author Simon Josef Kreuzpointner
 */
@Component
@Slf4j
public class LineServiceImpl implements LineService {

    private final LineRepository lineRepository;
    private final LineMapper lineMapper;
    private final LineValidation lineValidation;
    private final AuthorizationService authorizationService;
    private final ScriptRepository scriptRepository;

    public LineServiceImpl(LineRepository lineRepository, LineMapper lineMapper, LineValidation lineValidation, AuthorizationService authorizationService, ScriptRepository scriptRepository) {
        this.lineRepository = lineRepository;
        this.lineMapper = lineMapper;
        this.lineValidation = lineValidation;
        this.authorizationService = authorizationService;
        this.scriptRepository = scriptRepository;
    }

    @Transactional
    @Override
    public LineDto update(UpdateLineDto updateLineDto, Long sid, Long id) {
        log.trace("update(updateLineDto = {}, sid = {}, id = {})", updateLineDto, sid, id);

        if (updateLineDto.getContent() != null) {
            lineValidation.validateContentInput(updateLineDto.getContent());
        }

        User user = authorizationService.getLoggedInUser();
        if (user == null) {
            throw new UnauthorizedException();
        }
        Optional<Script> script = scriptRepository.findById(sid);
        if (script.isPresent() && !script.get().getOwner().getId().equals(user.getId())) {
            throw new UnauthorizedException("Der Nutzer darf diese Zeile nicht bearbeiten.");
        }

        Optional<Line> lineOptional = lineRepository.findById(id);
        Line line;
        if (lineOptional.isPresent()) {
            line = lineOptional.get();
        } else {
            throw new NotFoundException("Zeile existiert nicht!");
        }
        if (updateLineDto.getContent() != null) {
            line.setContent(updateLineDto.getContent());
        }
        if (updateLineDto.getIsInactive() != null) {
            line.setActive(updateLineDto.getIsInactive());
        }

        // TODO: delete all affected sessions

        return lineMapper.lineToLineDto(lineRepository.save(line));
    }

    @Transactional
    @Override
    public void delete(Long sid, Long id) {
        log.trace("delete(sid = {}, id = {})", sid, id);

        User user = authorizationService.getLoggedInUser();
        if (user == null) {
            throw new UnauthorizedException();
        }
        Optional<Script> script = scriptRepository.findById(sid);
        if (script.isPresent() && !script.get().getOwner().getId().equals(user.getId())) {
            throw new UnauthorizedException("Der Nutzer darf diese Zeile nicht löschen.");
        }
        Optional<Line> line = lineRepository.findById(id);
        if (line.isEmpty()) {
            throw new NotFoundException("Zeile existiert nicht!");
        }

        // TODO: delete all affected sessions

        lineRepository.deleteById(id);
    }
}
