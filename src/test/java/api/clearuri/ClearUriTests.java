package api.clearuri;

import io.qameta.allure.Feature;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import services.clearuri.ClearUriController;
import testmodels.requests.CorrectRequest;
import testmodels.requests.IncorrectRequest;
import testmodels.responses.SuccessCleanResponse;
import testmodels.responses.UnsuccessCleanResponse;

import java.util.Set;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.*;

@Feature("ShortenUri")
public class ClearUriTests extends BaseClearUriTest {

    private static final String CLEAN_URI_DOMEN = "https://cleanuri.com/";
    private static final String EMPTY_URI_SENT_ERROR = "API Error: After sanitization URL is empty";
    private static final String INCORRECT_URI_SENT_ERROR = "API Error: URL is invalid";
    private static final String INCORRECT_KEY_SENT_ERROR = "API Error: URL is empty";

    @Test
    @Tag("smoke")
    @DisplayName("Сокращение ссылки. Проверка работоспособности")
    public void cleanUriHealthCheck() {
        CorrectRequest request = getCorrectRequest(urlToClean);
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

    @Test
    @Tag("critical")
    @DisplayName("Сокращение ссылки. Ссылка сокращена")
    public void cleanUriReduceCheck() {
        CorrectRequest request = getCorrectRequest(urlToClean);
        Response response = step("Делаем запрос на укорачивание ссылки",
                () -> ClearUriController.getSentRequest(Method.POST, request));
        step("Проверяем, что в ответе ссылка укорочена",
                () -> assertTrue(response
                                .as(SuccessCleanResponse.class)
                                .getResult_url()
                                .startsWith(CLEAN_URI_DOMEN),
                        "Сокращенная ссылка должна начинаться на " + CLEAN_URI_DOMEN)
        );
    }

    @Test
    @Tag("negative")
    @DisplayName("Сокращение ссылки. Ссылка не отправлена")
    public void errorAfterEmptyUriSendCheck() {
        CorrectRequest request = getCorrectRequest("");
        Response response = step("Делаем запрос на укорачивание ссылки",
                () -> ClearUriController.getSentRequest(Method.POST, request));
        step("Проверяем, что в ответе присутствует ошибка",
                () -> {
                    assertEquals(HttpStatus.SC_BAD_REQUEST,
                            response.statusCode(),
                            "Status code должен быть = " + HttpStatus.SC_OK);
                    assertEquals(EMPTY_URI_SENT_ERROR, response
                                    .as(UnsuccessCleanResponse.class)
                                    .getError(),
                            "Текст ошибки при отправленной пустой ссылке должен быть = " + EMPTY_URI_SENT_ERROR);
                });
    }

    @ParameterizedTest(name = ", запрос с методом {0}")
    @MethodSource("getHttpBadMethods")
    @Tag("negative")
    @DisplayName("Сокращение ссылки. Отправка запроса с некорректным методом")
    public void errorAfterBadMethodCheck(Method method) {
        CorrectRequest request = getCorrectRequest(urlToClean);
        Response response = step("Делаем запрос на укорачивание ссылки",
                () -> ClearUriController.getSentRequest(method, request));
        step("Проверяем, что в получен status code" + HttpStatus.SC_METHOD_NOT_ALLOWED,
                () -> assertEquals(HttpStatus.SC_METHOD_NOT_ALLOWED,
                        response.statusCode(),
                        "Status code должен быть = " + HttpStatus.SC_METHOD_NOT_ALLOWED)
        );
    }

    @Test
    @Tag("negative")
    @DisplayName("Сокращение ссылки. Ошибка при отправленной сломанной ссылке")
    public void errorAfterIncorrectUriSendCheck() {
        CorrectRequest request = step("Ломаем ссылку", () -> getCorrectRequest(urlToClean
                .replace("://", "")));
        Response response = step("Делаем запрос на укорачивание ссылки",
                () -> ClearUriController.getSentRequest(Method.POST, request));
        step("Проверяем, что в ответе присутствует ошибка",
                () -> {
                    assertEquals(HttpStatus.SC_BAD_REQUEST,
                            response.statusCode(),
                            "Status code должен быть = " + HttpStatus.SC_OK);
                    assertTrue(response
                                    .as(UnsuccessCleanResponse.class)
                                    .getError()
                                    .startsWith(INCORRECT_URI_SENT_ERROR),
                            "Текст ошибки при отправленной некорректной ссылке должен быть = "
                                    + INCORRECT_URI_SENT_ERROR);
                });
    }

    @Test
    @Tag("negative")
    @DisplayName("Сокращение ссылки. Ошибка при отправленном неправильном ключе")
    public void errorAfterIncorrectKeySendCheck() {
        IncorrectRequest request = getIncorrectRequest(urlToClean);
        Response response = step("Делаем запрос на укорачивание ссылки",
                () -> ClearUriController.getSentRequest(Method.POST, request));
        step("Проверяем, что в ответе присутствует ошибка",
                () -> {
                    assertEquals(HttpStatus.SC_BAD_REQUEST,
                            response.statusCode(),
                            "Status code должен быть = " + HttpStatus.SC_OK);
                    assertTrue(response
                                    .as(UnsuccessCleanResponse.class)
                                    .getError()
                                    .startsWith(INCORRECT_KEY_SENT_ERROR),
                            "Текст ошибки при отправленном некорректном ключе должен быть = "
                                    + INCORRECT_KEY_SENT_ERROR);
                });
    }

    private static Set<Method> getHttpBadMethods() {
        return Set.of(Method.PUT, Method.GET, Method.DELETE, Method.PATCH);
    }
}
