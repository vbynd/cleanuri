package api.clearuri;

import io.qameta.allure.Epic;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

@Epic("clearUri")
public class BaseClearUriTest {
    protected static final String URL = "https://cleanuri.com/";
    protected static final String recoursePartOfURI = "api/v1/shorten";
    protected static final String inputFileWithUrl = "./src/test/resources/urlToClean.txt";
    protected static final String successResponseSchema = "./src/test/resources/successResponseSchema.json";
    protected static final String errorResponseSchema = "./src/test/resources/errorResponseSchema.json";
    protected String urlToClean;

    @BeforeEach
    public void setUp() {
        File file = new File(inputFileWithUrl);
        try {
            Scanner sc = new Scanner(file);
            urlToClean = sc.next();
            sc.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
