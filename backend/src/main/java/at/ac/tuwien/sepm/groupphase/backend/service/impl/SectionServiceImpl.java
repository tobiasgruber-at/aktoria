package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SectionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleSectionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.SectionMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Line;
import at.ac.tuwien.sepm.groupphase.backend.entity.Section;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UnauthorizedException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.LineRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectionRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.AuthorizationService;
import at.ac.tuwien.sepm.groupphase.backend.service.SectionService;
import at.ac.tuwien.sepm.groupphase.backend.validation.SectionValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Service for Section.
 *
 * @author Julia Bernold
 */
@Service
@Slf4j
public class SectionServiceImpl implements SectionService {
    private final SectionRepository sectionRepository;
    private final AuthorizationService authorizationService;
    private final SectionMapper sectionMapper;
    private final SectionValidation sectionValidation;
    private final LineRepository lineRepository;

    @Autowired
    public SectionServiceImpl(
        SectionRepository sectionRepository,
        AuthorizationService authorizationService,
        SectionMapper sectionMapper,
        SectionValidation sectionValidation,
        LineRepository lineRepository
    ) {
        this.sectionRepository = sectionRepository;
        this.authorizationService = authorizationService;
        this.sectionMapper = sectionMapper;
        this.sectionValidation = sectionValidation;
        this.lineRepository = lineRepository;
    }


    @Transactional
    @Override
    public SectionDto createSection(SimpleSectionDto simpleSectionDto) {
        log.trace("createSection(simpleSectionDto = {})", simpleSectionDto);
        User user = authorizationService.getLoggedInUser();
        if (user == null) {
            throw new UnauthorizedException();
        }
        authorizationService.checkBasicAuthorization(user.getId());
        sectionValidation.validateCreateSection(simpleSectionDto);
        Optional<Line> start = lineRepository.findById(simpleSectionDto.getStartLineId());
        Optional<Line> end = lineRepository.findById(simpleSectionDto.getEndLineId());
        if (start.isEmpty() || end.isEmpty()) {
            throw new ValidationException("Start oder Ende existiert nicht");
        }
        Section section = Section
            .builder()
            .id(null)
            .name(simpleSectionDto.getName())
            .owner(user)
            .startLine(start.get())
            .endLine(end.get())
            .sessions(null)
            .build();
        Section temp = sectionRepository.save(section);
        return sectionMapper.sectionToSectionDto(temp);
    }

    @Transactional
    @Override
    public void deleteSection(Long id) {
        log.trace("deleteSection(id = {})", id);
        User user = authorizationService.getLoggedInUser();
        if (user == null) {
            throw new UnauthorizedException();
        }
        authorizationService.checkBasicAuthorization(user.getId());
        Optional<Section> section = sectionRepository.findById(id);
        if (section.isEmpty()) {
            throw new NotFoundException("Abschnitt existiert nicht");
        }
        try {
            sectionValidation.ownerLoggedIn(section.get().getOwner().getId());
        } catch (UnauthorizedException e) {
            throw new UnauthorizedException(e.getMessage(), e);
        }
        sectionValidation.validateOwner(user.getId(), section.get().getStartLine().getId());
        sectionRepository.deleteById(id);
    }

    @Transactional
    @Override
    public SectionDto getSection(Long id) {
        log.trace("getSection(id = {}", id);
        User user = authorizationService.getLoggedInUser();
        if (user == null) {
            throw new UnauthorizedException();
        }
        authorizationService.checkBasicAuthorization(user.getId());

        Optional<Section> section = sectionRepository.findById(id);
        if (section.isEmpty()) {
            throw new NotFoundException("Abschnitt existiert nicht");
        }
        try {
            sectionValidation.ownerLoggedIn(section.get().getOwner().getId());
        } catch (UnauthorizedException e) {
            throw new UnauthorizedException(e.getMessage(), e);
        }
        SectionDto sectionDto = sectionMapper.sectionToSectionDto(section.get());
        sectionValidation.validateOwner(user.getId(), sectionDto.getStartLine().getId());
        return sectionDto;
    }

    @Transactional
    @Override
    public Stream<SectionDto> getAllSections() {
        log.trace("getAllSections()");
        User user = authorizationService.getLoggedInUser();
        return sectionRepository.findByOwner(user).stream().map(sectionMapper::sectionToSectionDto);
    }

    @Override
    public Stream<SectionDto> getAllSectionsByScript(Long id) {
        log.trace("getAllSectionsByScript(id = {})", id);
        User user = authorizationService.getLoggedInUser();
        if (user == null) {
            throw new UnauthorizedException();
        }
        authorizationService.checkBasicAuthorization(user.getId());
        if (id == null) {
            return getAllSections();
        }
        return sectionRepository.findByOwnerAndScriptId(user, id).stream().map(sectionMapper::sectionToSectionDto);
    }
}
