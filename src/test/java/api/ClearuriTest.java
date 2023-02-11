package api;

import api.PojoClasses.CorrectRequest;
import api.PojoClasses.IncorrectRequest;
import api.PojoClasses.SuccessClean;
import api.PojoClasses.UnsuccessClean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static io.restassured.RestAssured.given;

public class ClearuriTest {
    private static final String URL = "https://cleanuri.com/";
    private static final String recoursePartOfURI = "api/v1/shorten";
    private static final String pathToInputFile = "C:\\Additional\\IntelliJ IDEA Community Edition 2020.1.1\\Projects\\cleanuri\\src\\test\\java\\api\\urlToClean.txt";
    private String urlToClean;

    @BeforeEach
    public void setUp() {
        File file = new File(pathToInputFile);
        try {
            Scanner sc = new Scanner(file);
            urlToClean = sc.next();
            sc.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void successfullyCleanedUrl() {
        Specifications.installSpecification(Specifications.requestSpecification(URL), Specifications.responseSpecificationOK200());

        CorrectRequest request = new CorrectRequest(urlToClean);

        SuccessClean successClean = given()
                .body(request)
                .when()
                .post(recoursePartOfURI)
                .then()
                .extract().as(SuccessClean.class);

        Assertions.assertTrue(successClean.getResult_url().startsWith("https://cleanuri.com/"));
    }

    @Test
    public void errorAfterIncorrectMethodSend() {
        Specifications.installSpecification(Specifications.requestSpecification(URL), Specifications.responseSpecificationMETHODNOTALLOWED405());

        CorrectRequest request = new CorrectRequest(urlToClean);

        given()
                .when()
                .get(recoursePartOfURI);

        given()
                .body(request)
                .when()
                .put(recoursePartOfURI);

        given()
                .body(request)
                .when()
                .patch(recoursePartOfURI);

        given()
                .body(request)
                .when()
                .delete(recoursePartOfURI);
    }

    @Test
    public void errorAfterEmptyURLSend() {
        Specifications.installSpecification(Specifications.requestSpecification(URL), Specifications.responseSpecificationBADREQUEST400());

        CorrectRequest request = new CorrectRequest("");

        UnsuccessClean unsuccessClean = given()
                .body(request)
                .when()
                .post(recoursePartOfURI)
                .then()
                .extract().as(UnsuccessClean.class);

        Assertions.assertEquals("API Error: After sanitization URL is empty", unsuccessClean.getError());
    }

    @Test
    public void errorAfterIncorrectURLSend() {
        Specifications.installSpecification(Specifications.requestSpecification(URL), Specifications.responseSpecificationBADREQUEST400());

        CorrectRequest request = new CorrectRequest(urlToClean.replace("://", ""));

        UnsuccessClean unsuccessClean = given()
                .body(request)
                .when()
                .post(recoursePartOfURI)
                .then()
                .extract().as(UnsuccessClean.class);

        Assertions.assertTrue(unsuccessClean.getError().startsWith("API Error: URL is invalid"));
    }

    @Test
    public void errorAfterIncorrectKeySend() {
        Specifications.installSpecification(Specifications.requestSpecification(URL), Specifications.responseSpecificationBADREQUEST400());

        IncorrectRequest request = new IncorrectRequest(urlToClean);

        UnsuccessClean unsuccessClean = given()
                .body(request)
                .when()
                .post(recoursePartOfURI)
                .then()
                .extract().as(UnsuccessClean.class);

        Assertions.assertEquals("API Error: URL is empty", unsuccessClean.getError());
    }
}
