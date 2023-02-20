package api;

import api.pojoClasses.CorrectRequest;
import api.pojoClasses.IncorrectRequest;
import api.pojoClasses.SuccessClean;
import api.pojoClasses.UnsuccessClean;
import io.restassured.module.jsv.JsonSchemaValidator;
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
    private static final String inputFileWithUrl = "./src/test/resources/urlToClean.txt";
    private static final String successResponseSchema = "./src/test/resources/successResponseSchema.json";
    private static final String errorResponseSchema = "./src/test/resources/errorResponseSchema.json";
    private String urlToClean;

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

    @Test
    public void successfullyCleanedUrl() {
        Specifications.installSpecification(Specifications.requestSpecification(URL));

        CorrectRequest request = new CorrectRequest(urlToClean);

        SuccessClean successClean = given()
                .body(request)
                .when()
                .post(recoursePartOfURI)
                .then()
                .statusCode(200)
                .assertThat().body(JsonSchemaValidator.matchesJsonSchema(new File(successResponseSchema)))
                .extract().as(SuccessClean.class);

        Assertions.assertTrue(successClean.getResult_url().startsWith("https://cleanuri.com/"));
    }

    @Test
    public void error405AfterGetMethodSend() {
        Specifications.installSpecification(Specifications.requestSpecification(URL));

        CorrectRequest request = new CorrectRequest(urlToClean);

        given()
                .when()
                .get(recoursePartOfURI)
                .then()
                .statusCode(405);
    }

    @Test
    public void error405AfterPutMethodSend() {
        Specifications.installSpecification(Specifications.requestSpecification(URL));

        CorrectRequest request = new CorrectRequest(urlToClean);

        given()
                .body(request)
                .when()
                .put(recoursePartOfURI)
                .then()
                .statusCode(405);
    }

    @Test
    public void error405AfterPatchMethodSend() {
        Specifications.installSpecification(Specifications.requestSpecification(URL));

        CorrectRequest request = new CorrectRequest(urlToClean);

        given()
                .body(request)
                .when()
                .patch(recoursePartOfURI)
                .then()
                .statusCode(405);
    }

    @Test
    public void error405AfterDeleteMethodSend() {
        Specifications.installSpecification(Specifications.requestSpecification(URL));

        CorrectRequest request = new CorrectRequest(urlToClean);

        given()
                .body(request)
                .when()
                .delete(recoursePartOfURI)
                .then()
                .statusCode(405);
    }

    @Test
    public void errorAfterEmptyURLSend() {
        Specifications.installSpecification(Specifications.requestSpecification(URL));

        CorrectRequest request = new CorrectRequest("");

        UnsuccessClean unsuccessClean = given()
                .body(request)
                .when()
                .post(recoursePartOfURI)
                .then()
                .statusCode(400)
                .assertThat().body(JsonSchemaValidator.matchesJsonSchema(new File(errorResponseSchema)))
                .extract().as(UnsuccessClean.class);

        Assertions.assertEquals("API Error: After sanitization URL is empty", unsuccessClean.getError());
    }

    @Test
    public void errorAfterIncorrectURLSend() {
        Specifications.installSpecification(Specifications.requestSpecification(URL));

        CorrectRequest request = new CorrectRequest(urlToClean.replace("://", ""));

        UnsuccessClean unsuccessClean = given()
                .body(request)
                .when()
                .post(recoursePartOfURI)
                .then()
                .statusCode(400)
                .assertThat().body(JsonSchemaValidator.matchesJsonSchema(new File(errorResponseSchema)))
                .extract().as(UnsuccessClean.class);

        Assertions.assertTrue(unsuccessClean.getError().startsWith("API Error: URL is invalid"));
    }

    @Test
    public void errorAfterIncorrectKeySend() {
        Specifications.installSpecification(Specifications.requestSpecification(URL));

        IncorrectRequest request = new IncorrectRequest(urlToClean);

        UnsuccessClean unsuccessClean = given()
                .body(request)
                .when()
                .post(recoursePartOfURI)
                .then()
                .statusCode(400)
                .assertThat().body(JsonSchemaValidator.matchesJsonSchema(new File(errorResponseSchema)))
                .extract().as(UnsuccessClean.class);

        Assertions.assertEquals("API Error: URL is empty", unsuccessClean.getError());
    }
}
