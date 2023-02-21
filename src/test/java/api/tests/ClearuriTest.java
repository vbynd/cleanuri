package api.tests;

import api.requests.CorrectRequest;
import api.requests.IncorrectRequest;
import api.responses.SuccessClean;
import api.responses.UnsuccessClean;
import api.specifications.ReqSpecification;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.given;

public class ClearuriTest extends BaseTest{

    @Test
    public void successfullyCleanedUrl() {
        ReqSpecification.installSpecification(ReqSpecification.requestSpecification(URL));

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
        ReqSpecification.installSpecification(ReqSpecification.requestSpecification(URL));

        CorrectRequest request = new CorrectRequest(urlToClean);

        given()
                .when()
                .get(recoursePartOfURI)
                .then()
                .statusCode(405);
    }

    @Test
    public void error405AfterPutMethodSend() {
        ReqSpecification.installSpecification(ReqSpecification.requestSpecification(URL));

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
        ReqSpecification.installSpecification(ReqSpecification.requestSpecification(URL));

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
        ReqSpecification.installSpecification(ReqSpecification.requestSpecification(URL));

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
        ReqSpecification.installSpecification(ReqSpecification.requestSpecification(URL));

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
        ReqSpecification.installSpecification(ReqSpecification.requestSpecification(URL));

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
        ReqSpecification.installSpecification(ReqSpecification.requestSpecification(URL));

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
