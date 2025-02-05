package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SectionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleSectionDto;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

/**
 * Service for section.
 *
 * @author Julia Bernold
 */
@Service
public interface SectionService {

    /**
     * Creates a new section.
     *
     * @param simpleSectionDto the section to be created
     * @return the section that was created
     */
    SectionDto createSection(SimpleSectionDto simpleSectionDto);

    /**
     * Deletes an existing section.
     *
     * @param id the ID of the section to be deleted
     */
    void deleteSection(Long id);

    /**
     * Gets the section with the specified ID.
     *
     * @param id the ID of the requested section
     * @return the section with the specified ID
     */
    SectionDto getSection(Long id);

    /**
     * Gets all sections stored in the database.
     *
     * @return all sections stored in the database
     */
    Stream<SectionDto> getAllSections();

    /**
     * Gets all sections stored in the database by script.
     *
     * @param id of the script
     * @return all sections by script
     */
    Stream<SectionDto> getAllSectionsByScript(Long id);
}
