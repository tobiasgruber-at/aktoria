package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ScriptDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.StagedScriptDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.IllegalFileFormatException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import org.springframework.web.multipart.MultipartFile;

/**
 * Describes a script service component.
 *
 * @author Simon Josef Kreuzpointner
 */
public interface ScriptService {

    /**
     * Processes a new pdf script file.
     * <br>
     * Throws a ServiceException when the given file is corrupted or not found.
     * Throws an IllegalFileFormatException, when the raw bytes of the given file do
     * not start with the PDF header and the PDF trailer is not found at the end of the file.
     *
     * @param file a script file
     * @return a new instance of StagedScriptDto
     * @throws ServiceException           when an error occurs while trying to process the file
     * @throws IllegalFileFormatException when the given file is not a pdf
     */
    StagedScriptDto create(MultipartFile file) throws ServiceException, IllegalFileFormatException;

    /**
     * Saves a given script in the data storage.
     *
     * @param scriptDto the scrip to be saved
     * @return the saved script
     * @throws ServiceException when an error occurs while trying to process the script
     */
    ScriptDto save(ScriptDto scriptDto) throws ServiceException;
}
