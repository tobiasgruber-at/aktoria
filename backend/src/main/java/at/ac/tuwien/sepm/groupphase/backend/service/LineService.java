package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LineDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UpdateLineDto;
import org.springframework.stereotype.Component;

/**
 * Describes a line service component.
 *
 * @author Simon Josef Kreuzpointner
 */
@Component
public interface LineService {

    /**
     * Updates a line.
     * <br>
     * Sets the content of the line of the given id.
     *
     * @param updateLineDto changes to the line
     * @param id            the id of the line to be changed
     * @return the updated line
     */
    LineDto update(UpdateLineDto updateLineDto, Long id);
}
