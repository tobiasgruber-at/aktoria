package at.ac.tuwien.sepm.groupphase.backend.service.parsing.script;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class ScriptUnitTest {

    @Test
    @DisplayName("getFileContentsAsPlainText() returns the correct text")
    void getFileContentsAsPlainText() throws IOException {
        File inputFile = new File("./src/test/resources/service/parsing/script/Skript_NF.pdf");
        String expected = Files.readString(Path.of("./src/test/resources/service/parsing/script/Skript_NF_expected.txt"));
        MultipartFile multipartFile = new MockMultipartFile("file", new FileInputStream(inputFile));

        Script s = new Script(multipartFile);

        assertThat(expected).isEqualToNormalizingNewlines(s.getFileContentsAsPlainText());
    }
}