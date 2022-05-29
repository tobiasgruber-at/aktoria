package at.ac.tuwien.sepm.groupphase.backend.validation.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SectionDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Line;
import at.ac.tuwien.sepm.groupphase.backend.entity.Script;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UnauthorizedException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.LineRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.AuthorizationService;
import at.ac.tuwien.sepm.groupphase.backend.validation.SectionValidation;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

/**
 * Implementation of SectionValidation.
 *
 * @author Julia Bernold
 */
@Component
public class SectionValidationImpl implements SectionValidation {
    UserRepository userRepository;
    LineRepository lineRepository;
    AuthorizationService authorizationService;

    public SectionValidationImpl(
        UserRepository userRepository,
        LineRepository lineRepository,
        AuthorizationService authorizationService) {
        this.userRepository = userRepository;
        this.lineRepository = lineRepository;
        this.authorizationService = authorizationService;
    }

    @Override
    public void validateCreateSection(SectionDto sectionDto) {
        try {
            validateName(sectionDto.getName());
            validateOwner(sectionDto.getOwner());
            validateLines(sectionDto.getStartLine(), sectionDto.getEndLine());
            ownerLoggedIn(sectionDto.getOwner());
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage(), e);
        } catch (UnauthorizedException e) {
            throw new UnauthorizedException(e.getMessage(), e);
        }
    }

    private void validateName(String name) throws ValidationException {
        if (name.length() == 0) {
            throw new ValidationException("Name muss mehr als 0 Zeichen haben!");
        }
        if (name.trim().length() == 0) {
            throw new ValidationException("Name darf nicht nur aus Leerzeichen bestehen!");
        }
        if (name.length() > 100) {
            throw new ValidationException("Name darf nicht länger als 100 Zeichen sein!");
        }
    }

    private void validateOwner(Long ownerId) throws NotFoundException {
        if (ownerId == null) {
            throw new ValidationException("Lernabschnitt hat keinen Besitzer");
        }
        Optional<User> user = userRepository.findById(ownerId);
        if (user.isEmpty()) {
            throw new ValidationException("Besitzer des Lernabschnitts existiert nicht!");
        }
    }

    private void validateLines(Long startId, Long endId) throws ValidationException {
        if (startId == null) {
            throw new ValidationException("Lernabschnitt hat keinen Anfang");
        }
        if (endId == null) {
            throw new ValidationException("Lernabschnitt hat kein Ende");
        }
        if (startId > endId) {
            throw new ValidationException("Anfang muss vor dem Ende liegen!");
        }
        Optional<Line> start = lineRepository.findById(startId);
        Optional<Line> end = lineRepository.findById(endId);
        if (start.isEmpty()) {
            throw new ValidationException("Anfang des Lernabschnitts existiert nicht");
        }
        if (end.isEmpty()) {
            throw new ValidationException("Ende des Lernabschnitts existiert nicht");
        }
        Script startScript = start.get().getPage().getScript();
        Script endScript = end.get().getPage().getScript();
        if (!Objects.equals(startScript.getId(), endScript.getId())) {
            throw new ValidationException("Start und Ende müssen im selben Skript liegen!");
        }
    }

    public void ownerLoggedIn(Long ownerId) throws UnauthorizedException {
        User loggedIn = authorizationService.getLoggedInUser();
        if (!Objects.equals(ownerId, loggedIn.getId())) {
            throw new UnauthorizedException("Ersteller des Lernabschnitts ist nicht der eingeloggte Nutzer!");
        }
    }
}
