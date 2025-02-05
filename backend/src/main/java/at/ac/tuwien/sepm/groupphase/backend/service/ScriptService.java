package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ScriptDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ScriptPreviewDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleScriptDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UpdateScriptDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.IllegalFileFormatException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
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
    SimpleScriptDto parse(MultipartFile file, Integer startPage);

    /**
     * Saves a given script in the data storage.
     *
     * @param simpleScriptDto the scrip to be saved
     * @return the saved script
     * @throws ServiceException when an error occurs while trying to process the script
     */
    ScriptDto save(SimpleScriptDto simpleScriptDto);

    /**
     * Gets all script in the form of script preview data access objects.
     *
     * @return a stream of previews
     * @throws ServiceException when an error occurs while trying to get the scripts
     */
    Stream<ScriptPreviewDto> findAllPreviews();

    /**
     * Gets the script corresponding to the given id.
     *
     * @param id the id of the wanted script
     * @return the script corresponding to the given id.
     * @throws ServiceException when an error occurs while trying to get the script
     */
    ScriptDto findById(Long id);

    /**
     * Deletes script with given id from the data storage.
     *
     * @param id the id of the script to be deleted
     * @author Marvin Flandorfer
     */
    void delete(Long id);

    /**
     * Updates script with given id.
     *
     * @param updateScriptDto the updates to be performed
     * @return the updated script
     */
    ScriptDto update(UpdateScriptDto updateScriptDto, Long id);

    /**
     * Sends an invitation to join the script as participant.
     *
     * @param scriptId id of the script
     * @param email    email the invitation is sent to
     */
    void invite(Long scriptId, String email);

    /**
     * Accepts an invitaion and add user to participants.
     *
     * @param id    of the new participant
     * @param token to verify that an invitation was received
     */
    void addParticipant(Long id, String token);

    /**
     * Returns an invitationlink to join the script as participant.
     *
     * @param scriptId id of the script
     */
    String inviteLink(Long scriptId);


    /**
     * Returns a Script dto of a session.
     *
     * @param id of the session
     * @return a script
     */
    ScriptDto getBySessionId(Long id);

    /**
     * Deletes a participant from a script.
     *
     * @param scriptId script to delete participant from
     * @param email    email of the user who wants edit this script
     */

    void deleteParticipant(Long scriptId, String email);
}