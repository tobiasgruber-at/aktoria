package at.ac.tuwien.sepm.groupphase.backend.service.parsing.script;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public class Script {
    private final File pdfFile;

    public Script(File pdfFile) {
        this.pdfFile = pdfFile;
    }

    public String getFileContentsAsPlainText() throws IOException {
        PDDocument document = PDDocument.load(pdfFile);
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
