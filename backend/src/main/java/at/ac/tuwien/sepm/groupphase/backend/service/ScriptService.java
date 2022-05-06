package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.StagedScriptDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.exceptionhandler.ServiceException;

import java.io.File;

/**
 * Describes a script service component.
 *
 * @author Simon Josef Kreuzpointner
 */
public interface ScriptService {

    /**
     * Processes a new pdf script file.
     * <br>
     * Throws a ServiceException when the given file is corrupted, not found or
     * does not have the pdf mime type.
     *
     * @param pdfScript a script file
     * @return a new instance of StagedScriptDto
     * @throws ServiceException when an error occurs while trying to process the file
     */
    StagedScriptDto newScript(File pdfScript) throws ServiceException;
}
