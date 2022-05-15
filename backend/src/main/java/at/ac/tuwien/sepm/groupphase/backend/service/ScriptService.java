package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ScriptDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ScriptPreviewDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleScriptDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.IllegalFileFormatException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UnauthorizedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Stream;

/**
 * Describes a script service component.
 *
 * @author Simon Josef Kreuzpointner
 */
@Service
public interface ScriptService {

    /**
     * Processes a new pdf script file.
     * <br>
     * Throws a ServiceException when the given file is corrupted or not found.
     * Throws an IllegalFileFormatException, when the raw bytes of the given file do
     * not start with the PDF header and the PDF trailer is not found at the end of the file.
     *
     * @param file      a script file
     * @param startPage the start of the play in the script
     * @return a new instance of SimpleScriptDto
     * @throws ServiceException           when an error occurs while trying to process the file
     * @throws IllegalFileFormatException when the given file is not a pdf
     */
    SimpleScriptDto parse(MultipartFile file, Integer startPage) throws ServiceException, IllegalFileFormatException;

    /**
     * Saves a given script in the data storage.
     *
     * @param simpleScriptDto the scrip to be saved
     * @return the saved script
     * @throws ServiceException when an error occurs while trying to process the script
     */
    ScriptDto save(SimpleScriptDto simpleScriptDto) throws ServiceException;

    /**
     * Gets all script in the form of script preview data access objects.
     *
     * @return a stream of previews
     * @throws ServiceException when an error occurs while trying to get the scripts
     */
    Stream<ScriptPreviewDto> findAllPreviews() throws ServiceException;

    /**
     * Gets the script corresponding to the given id.
     *
     * @param id the id of the wanted script
     * @return the script corresponding to the given id.
     * @throws ServiceException when an error occurs while trying to get the script
     */
    ScriptDto findById(Long id) throws ServiceException, NotFoundException, UnauthorizedException;
}
