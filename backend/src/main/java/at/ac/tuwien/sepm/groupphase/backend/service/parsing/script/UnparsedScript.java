package at.ac.tuwien.sepm.groupphase.backend.service.parsing.script;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * An unparsed PDF script.
 *
 * @author Simon Josef Kreuzpointner
 */
@Slf4j
public class UnparsedScript {
    private final MultipartFile pdfFile;
    private final Integer startPage;

    public UnparsedScript(MultipartFile pdfFile, Integer startPage) {
        this.pdfFile = pdfFile;
        this.startPage = startPage;
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
        log.trace("getFileContentsAsPlainText()");

        PDDocument document = PDDocument.load(pdfFile.getBytes());

        if (document.isEncrypted()) {
            throw new IOException("Datei ist verschlüsselt");
        }

        PDFTextStripper stripper = new PDFTextStripper();

        stripper.setStartPage(startPage);
        stripper.setSortByPosition(true);
        stripper.setPageEnd("\n\n\f\n");
        stripper.setParagraphStart("\n");
        stripper.setDropThreshold(1.66f);

        String content = stripper.getText(document);
        document.close();

        return content;
    }
}
