package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LineDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.LineMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Line;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.LineRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.LineService;
import at.ac.tuwien.sepm.groupphase.backend.validation.LineValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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

    public LineServiceImpl(LineRepository lineRepository, LineMapper lineMapper, LineValidation lineValidation) {
        this.lineRepository = lineRepository;
        this.lineMapper = lineMapper;
        this.lineValidation = lineValidation;
    }

    @Override
    public LineDto update(String content, Long id) {
        log.trace("update(content = {}, id = {})", content, id);

        lineValidation.validateContentInput(content);
        Optional<Line> lineOptional = lineRepository.findById(id);
        Line line;
        if (lineOptional.isPresent()) {
            line = lineOptional.get();
        } else {
            throw new NotFoundException("Zeile existiert nicht!");
        }
        line.setContent(content);
        return lineMapper.lineToLineDto(lineRepository.save(line));
    }
}
