package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SectionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.SectionMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Section;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UnauthorizedException;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectionRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.AuthorizationService;
import at.ac.tuwien.sepm.groupphase.backend.service.SectionService;
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

    @Autowired
    public SectionServiceImpl(
        SectionRepository sectionRepository,
        AuthorizationService authorizationService,
        SectionMapper sectionMapper
    ) {
        this.sectionRepository = sectionRepository;
        this.authorizationService = authorizationService;
        this.sectionMapper = sectionMapper;
    }

    //TODO: Test, implement, validation
    @Override
    public SectionDto createSection(SectionDto sectionDto) {
        log.trace("createSection(sectionDto = {})", sectionDto);
        User user = authorizationService.getLoggedInUser();
        if (user == null) {
            throw new UnauthorizedException();
        }
        //TODO: validation
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
        //TODO: validation
        Optional<Section> section = sectionRepository.findById(id);
        if (section.isEmpty()) {
            throw new NotFoundException();
        }
        return sectionMapper.sectionToSectionDto(section.get());
    }
}
