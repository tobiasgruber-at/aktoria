package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SectionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.SectionMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Section;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UnauthorizedException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectionRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.AuthorizationService;
import at.ac.tuwien.sepm.groupphase.backend.service.SectionService;
import at.ac.tuwien.sepm.groupphase.backend.validation.SectionValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    @Autowired
    public SectionServiceImpl(
        SectionRepository sectionRepository,
        AuthorizationService authorizationService,
        SectionMapper sectionMapper,
        SectionValidation sectionValidation
    ) {
        this.sectionRepository = sectionRepository;
        this.authorizationService = authorizationService;
        this.sectionMapper = sectionMapper;
        this.sectionValidation = sectionValidation;
    }

    @Override
    public SectionDto createSection(SectionDto sectionDto) {
        log.trace("createSection(sectionDto = {})", sectionDto);
        User user = authorizationService.getLoggedInUser();
        if (user == null) {
            throw new UnauthorizedException();
        }
        try {
            sectionValidation.validateCreateSection(sectionDto);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage(), e);
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage(), e);
        } catch (UnauthorizedException e) {
            throw new UnauthorizedException(e.getMessage(), e);
        }
        Section section = sectionMapper.sectionDtoToSection(sectionDto);
        section = sectionRepository.save(section);
        return sectionMapper.sectionToSectionDto(section);
    }

    @Override
    public void deleteSection(Long id) {
        log.trace("deleteSection(id = {})", id);
        User user = authorizationService.getLoggedInUser();
        if (user == null) {
            throw new UnauthorizedException();
        }
        authorizationService.checkBasicAuthorization(user.getId());
        //TODO: validation
        Optional<Section> section = sectionRepository.findById(id);
        if (section.isEmpty()) {
            throw new NotFoundException();
        }
        try {
            sectionValidation.ownerLoggedIn(section.get().getOwner().getId());
        } catch (UnauthorizedException e) {
            throw new UnauthorizedException(e.getMessage(), e);
        }
        sectionRepository.deleteById(id);
    }

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
            throw new NotFoundException();
        }
        try {
            sectionValidation.ownerLoggedIn(section.get().getOwner().getId());
        } catch (UnauthorizedException e) {
            throw new UnauthorizedException(e.getMessage(), e);
        }
        return sectionMapper.sectionToSectionDto(section.get());
    }
}
