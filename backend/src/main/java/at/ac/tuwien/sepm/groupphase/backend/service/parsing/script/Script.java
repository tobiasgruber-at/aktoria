package at.ac.tuwien.sepm.groupphase.backend.service.parsing.script;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

/**
 * An unparsed PDF script.
 *
 * @author Simon Josef Kreuzpointner
 */
public class Script {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final MultipartFile pdfFile;

    public Script(MultipartFile pdfFile) {
        this.pdfFile = pdfFile;
    }

    /**
     * Gets the file contents as plain text.
     * <br>
     * This method gets the file contents as plain text. It ignores images, formatting
     * the text layout or any other decorations.
     * <br>
     * The page end is denoted with the form feed character \f.
     *
     * @return the file contents as a string
     * @throws IOException if the pdf file is corrupted or encrypted
     */
    public String getFileContentsAsPlainText() throws IOException {
        LOGGER.trace("getFileContentsAsPlainText()");

        PDDocument document = PDDocument.load(pdfFile.getBytes());

        if (document.isEncrypted()) {
            throw new IOException("File is encrypted.");
        }

        PDFTextStripper stripper = new PDFTextStripper();

        stripper.setSortByPosition(true);
        stripper.setPageEnd("\n\n\f\n");
        stripper.setParagraphStart("\n");
        stripper.setDropThreshold(1.66f);

        String content = stripper.getText(document);
        document.close();

        return content;
    }
}
