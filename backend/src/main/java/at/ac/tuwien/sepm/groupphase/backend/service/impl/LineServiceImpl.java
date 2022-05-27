package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LineDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UpdateLineDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.LineMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Line;
import at.ac.tuwien.sepm.groupphase.backend.entity.Page;
import at.ac.tuwien.sepm.groupphase.backend.entity.Role;
import at.ac.tuwien.sepm.groupphase.backend.entity.Script;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UnauthorizedException;
import at.ac.tuwien.sepm.groupphase.backend.repository.LineRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RoleRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ScriptRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.AuthorizationService;
import at.ac.tuwien.sepm.groupphase.backend.service.LineService;
import at.ac.tuwien.sepm.groupphase.backend.validation.LineValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    private final RoleRepository roleRepository;

    private final PageRepository pageRepository;

    public LineServiceImpl(LineRepository lineRepository, LineMapper lineMapper, LineValidation lineValidation, AuthorizationService authorizationService, ScriptRepository scriptRepository, RoleRepository roleRepository,
                           PageRepository pageRepository) {
        this.lineRepository = lineRepository;
        this.lineMapper = lineMapper;
        this.lineValidation = lineValidation;
        this.authorizationService = authorizationService;
        this.scriptRepository = scriptRepository;
        this.roleRepository = roleRepository;
        this.pageRepository = pageRepository;
    }

    @Transactional
    @Override
    public LineDto update(UpdateLineDto updateLineDto, Long id) {
        log.trace("update(updateLineDto = {}, id = {})", updateLineDto, id);

        if (updateLineDto.getContent() != null) {
            lineValidation.validateContentInput(updateLineDto.getContent());
        }
        if (updateLineDto.getRoleIds() != null) {
            lineValidation.validateRoleIdsInput(updateLineDto.getRoleIds());
        }
        User user = authorizationService.getLoggedInUser();
        if (user == null) {
            throw new UnauthorizedException();
        }
        Optional<Line> lineOptional = lineRepository.findById(id);
        Line line;
        if (lineOptional.isPresent()) {
            line = lineOptional.get();
        } else {
            throw new NotFoundException("Zeile existiert nicht!");
        }
        Optional<Page> pageOptional = pageRepository.findById(line.getPage().getId());
        Page page;
        if (pageOptional.isPresent()) {
            page = pageOptional.get();
        } else {
            throw new NotFoundException("Seite existiert nicht!");
        }
        Optional<Script> scriptOptional = scriptRepository.findById(page.getScript().getId());
        if (scriptOptional.isPresent() && !scriptOptional.get().getOwner().getId().equals(user.getId())) {
            throw new UnauthorizedException("Der Nutzer darf diese Zeile nicht bearbeiten.");
        }
        if (updateLineDto.getContent() != null) {
            line.setContent(updateLineDto.getContent());
        }
        if (updateLineDto.getActive() != null) {
            line.setActive(updateLineDto.getActive());
        }
        if (updateLineDto.getRoleIds() != null) {
            final List<Long> ids = updateLineDto.getRoleIds();
            if (ids.size() <= 0) {
                line.setSpokenBy(null);
            } else {
                List<Role> roles = roleRepository.findAllById(ids);
                line.setSpokenBy(new HashSet<>(Set.copyOf(roles)));
            }
        }

        // TODO: delete all affected sessions

        return lineMapper.lineToLineDto(lineRepository.save(line));
    }
}
