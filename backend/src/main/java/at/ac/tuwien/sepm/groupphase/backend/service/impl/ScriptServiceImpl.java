package at.ac.tuwien.sepm.groupphase.backend.service.impl;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ScriptDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.StagedScriptDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ParsedScriptMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.IllegalFileFormatException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.ScriptService;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.script.ParsedScript;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.script.Script;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.scriptparser.ScriptParser;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.scriptparser.impl.ScriptParserImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

/**
 * A specific implementation of ScriptService.
 *
 * @author Simon Josef Kreuzpointner
 */
@Service
public class ScriptServiceImpl implements ScriptService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public StagedScriptDto create(MultipartFile file) throws ServiceException {
        LOGGER.trace("newScript(pdfScript = {})", file);

        boolean isPdfFile;
        try {
            isPdfFile = isPdfFileType(file);
        } catch (IOException e) {
            throw new ServiceException(e.getMessage(), e);
        }

        if (!isPdfFile) {
            throw new IllegalFileFormatException("Illegal File Format.");
        }

        Script s = new Script(file);
        String raw;

        try {
            raw = s.getFileContentsAsPlainText();
        } catch (IOException e) {
            throw new ServiceException(e.getMessage(), e);
        }

        ScriptParser parser = new ScriptParserImpl(raw);
        ParsedScript parsedScript = parser.parse();

        return ParsedScriptMapper.INSTANCE.parsedScriptToScriptDto(parsedScript);
    }

    /**
     * Checks if the given file is of type pdf.
     *
     * @see <a href="https://sceweb.sce.uhcl.edu/abeysekera/itec3831/labs/FILE%20SIGNATURES%20TABLE.pdf"></a>
     */
    private boolean isPdfFileType(MultipartFile file) throws IOException {
        LOGGER.trace("isPdfFileType(file = {})", file);

        byte[] data = file.getBytes();

        return (
            data.length >= 4
                && data[0] == 0x25
                && data[1] == 0x50
                && data[2] == 0x44
                && data[3] == 0x46)
            && (
            data.length >= 10
                && data[data.length - 6] == 0x0a
                && data[data.length - 5] == 0x25
                && data[data.length - 4] == 0x25
                && data[data.length - 3] == 0x45
                && data[data.length - 2] == 0x4f
                && data[data.length - 1] == 0x46)
            || (
            data.length >= 11
                && data[data.length - 7] == 0x0a
                && data[data.length - 6] == 0x25
                && data[data.length - 5] == 0x25
                && data[data.length - 4] == 0x45
                && data[data.length - 3] == 0x4f
                && data[data.length - 2] == 0x46
                && data[data.length - 1] == 0x0a)
            || (
            data.length >= 13
                && data[data.length - 9] == 0x0d
                && data[data.length - 8] == 0x2a
                && data[data.length - 7] == 0x25
                && data[data.length - 6] == 0x25
                && data[data.length - 5] == 0x45
                && data[data.length - 4] == 0x4f
                && data[data.length - 3] == 0x46
                && data[data.length - 2] == 0x0d
                && data[data.length - 1] == 0x0a)
            || (
            data.length >= 11
                && data[data.length - 7] == 0x0d
                && data[data.length - 6] == 0x25
                && data[data.length - 5] == 0x25
                && data[data.length - 4] == 0x45
                && data[data.length - 3] == 0x4f
                && data[data.length - 2] == 0x46
                && data[data.length - 1] == 0x0d);
    }

    @Override
    public ScriptDto save(ScriptDto scriptDto) throws ServiceException {
        LOGGER.trace("save(scriptDto = {})", scriptDto);

        throw new UnsupportedOperationException();
    }
}
