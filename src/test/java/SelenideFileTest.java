import net.lingala.zip4j.core.ZipFile;
import org.junit.jupiter.api.Test;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static com.codeborne.selenide.Selenide.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SelenideFileTest {

    @Test
    void downloadFileTest() throws Exception {
        open("https://github.com/selenide/selenide/blob/master/README.md");
        File download = $("#raw-url").download();
        String result;

        try (InputStream is = new FileInputStream(download)) {
            result = new String(is.readAllBytes(), "UTF-8");
        }
        assertThat(result).contains("What is Selenide?");
    }

    @Test
    void TestTextFile() throws Exception {
        String result;

        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("testFile.txt")) {
            result = new String(stream.readAllBytes(), "UTF-8");
        }
        assertThat(result).contains("Проверка файла");
    }

    @Test
    void pdfFileTest() throws Exception {

            PDF parsed = new PDF(getClass().getClassLoader().getResourceAsStream("82.pdf"));
            assertThat(parsed.text).contains("Datalogic ADC, Inc.");
        }

    @Test
    void xlsFileTest() throws Exception {

        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("testXls.xlsx")) {
            XLS parsed = new XLS(stream);
            assertThat(parsed.excel.getSheetAt(0).getRow(2).getCell(1).getStringCellValue())
            .isEqualTo("Владимир");
        }
    }

    @Test
    void readDocTest() throws Exception {

        try (InputStream file = getClass().getClassLoader().getResourceAsStream("testDoc.docx")) {
            XWPFDocument document = new XWPFDocument(file);
            XWPFWordExtractor xwpfWordExtractor = new XWPFWordExtractor(document);
            String docText = xwpfWordExtractor.getText();
            assertThat(docText.contains("Проверка файла."));
        }
    }

    @Test
    void ZipTest() throws Exception {

        ZipFile zipFile = new ZipFile("./src/test/resources/ziptest.zip");
        if (zipFile.isEncrypted())
            zipFile.setPassword("589".toCharArray());
        zipFile.extractAll("./src/test/resources/12");

        try (FileInputStream stream = new FileInputStream("./src/test/resources/12/t3.txt")) {
            String result = new String(stream.readAllBytes(), "UTF-8");
            assertThat(result).contains("chek");
        }
    }
}


