package at.ac.tuwien.sepm.groupphase.backend.service.impl;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.StagedScriptDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ParsedScriptMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.ScriptService;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.script.ParsedScript;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.script.Script;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.scriptparser.ScriptParser;
import at.ac.tuwien.sepm.groupphase.backend.service.parsing.scriptparser.impl.ScriptParserImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A specific implementation of ScriptService.
 *
 * @author Simon Josef Kreuzpointner
 */
@Service
public class ScriptServiceImpl implements ScriptService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public StagedScriptDto newScript(File pdfScript) throws ServiceException {
        LOGGER.trace("newScript(pdfScript = {})", pdfScript);

        boolean isPdfFile;
        try {
            isPdfFile = isPdfFileType(pdfScript);
        } catch (IOException e) {
            throw new ServiceException(e.getMessage(), e);
        }

        if (!isPdfFile) {
            throw new ServiceException("Illegal File Format.");
        }

        Script s = new Script(pdfScript);
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

    private boolean isPdfFileType(File file) throws IOException {
        // https://sceweb.sce.uhcl.edu/abeysekera/itec3831/labs/FILE%20SIGNATURES%20TABLE.pdf

        byte[] data = Files.readAllBytes(Path.of(file.getPath()));

        // Header
        if (data.length >= 4
            && data[0] == 0x25
            && data[1] == 0x50
            && data[2] == 0x44
            && data[3] == 0x46) {

            // Trailer
            return (
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

        return false;
    }
}
