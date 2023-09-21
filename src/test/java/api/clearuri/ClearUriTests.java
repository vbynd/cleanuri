package api.clearuri;

import api.requests.CorrectRequest;
import api.requests.IncorrectRequest;
import api.responses.SuccessCleanResponse;
import api.responses.UnsuccessClean;
import api.specifications.ReqSpecification;
import io.qameta.allure.Feature;
import io.restassured.http.Method;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import services.clearuri.ClearUriController;

import java.util.Set;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Feature("ClearUri")
public class ClearUriTests extends BaseClearUriTest {

    @Test
    @Tag("smoke")
    @DisplayName("Сокращение ссылки. Проверка работоспособности")
    public void cleanUriHealthCheck() {
        CorrectRequest request = new CorrectRequest(urlToClean);
        Response response = step("Делаем запрос на укорачивание ссылки",
                () -> ClearUriController.getSentRequest(Method.POST, request));
        step("Проверяем, что в ответе присутствует укороченная ссылка",
                () -> {
                    assertEquals(HttpStatus.SC_OK,
                            response.statusCode(),
                            "Status code должен быть = " + HttpStatus.SC_OK);
                    assertFalse(response
                                    .as(SuccessCleanResponse.class)
                                    .getResult_url()
                                    .isEmpty(),
                            "Сокращенная ссылка не должна быть пустой");
                });
    }

    @ParameterizedTest(name = ", запрос с методом {0}")
    @MethodSource("getHttpBadMethods")
    @Tag("negative")
    @DisplayName("Сокращение ссылки. Отправка запроса с некорректным методом")
    public void errorAfterBadMethodCheck(Method method) {
        CorrectRequest request = new CorrectRequest(urlToClean);
        Response response = step("Делаем запрос на укорачивание ссылки",
                () -> ClearUriController.getSentRequest(method, request));
        step("Проверяем, что в получен status code" + HttpStatus.SC_METHOD_NOT_ALLOWED,
                () -> assertEquals(HttpStatus.SC_METHOD_NOT_ALLOWED,
                        response.statusCode(),
                        "Status code должен быть = " + HttpStatus.SC_METHOD_NOT_ALLOWED)
        );
    }

    private static Set<Method> getHttpBadMethods() {
        return Set.of(Method.PUT, Method.GET, Method.DELETE, Method.PATCH);
    }
//TODO: доделать остальные методы
/*    @Test
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
    }*/
}
