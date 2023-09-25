package api.clearuri;

import io.qameta.allure.Epic;
import org.junit.jupiter.api.BeforeAll;
import testmodels.requests.CorrectRequest;
import testmodels.requests.IncorrectRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

@Epic("ClearUri")
public class BaseClearUriTest {
    protected static final String INPUT_FILE_WITH_URL = "./src/test/resources/urlToClean.txt";
    protected static String urlToClean;

    @BeforeAll
    public static void setUp() {
        File file = new File(INPUT_FILE_WITH_URL);
        try {
            Scanner sc = new Scanner(file);
            urlToClean = sc.next();
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected static CorrectRequest getCorrectRequest(String urlToClean) {
        return new CorrectRequest(urlToClean);
    }

    protected static IncorrectRequest getIncorrectRequest(String urlToClean) {
        return new IncorrectRequest(urlToClean);
    }
}
