package services.clearuri;

import enums.PropertyEnum;
import enums.RegexpsEnum;
import helpers.PropertyLoader;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import testmodels.requests.Request;

import static io.restassured.RestAssured.given;

@Slf4j
public class ClearUriController {

    public static final String CLEAR_URI_SERVICE_URL = PropertyLoader.loadPropertyByName(PropertyEnum.CLEAR_URI_SERVICE_URL.name());
    public static final String CLEAR_URI_SERVICE_SHORTEN_URL = CLEAR_URI_SERVICE_URL + "/shorten";
    public static final int COUNT_OF_SYMBOLS_IN_LOG = 100;
    private static final RequestSpecification REQUEST_SPECIFICATION = new RequestSpecBuilder()
            .setBaseUri(CLEAR_URI_SERVICE_SHORTEN_URL)
            .setContentType(ContentType.JSON)
            .setRelaxedHTTPSValidation()
            .build();

    public static Response getSentRequest(Method httpMethod, Request body) {
        Response response = given()
                .spec(REQUEST_SPECIFICATION)
                .body(body)
                .filter(new AllureRestAssured())
                .when().request(httpMethod)
                .then()
                .extract().response();
        log.info("{}", response.asPrettyString()
                .replaceAll(RegexpsEnum.REGEXP_FOR_CUT_STRING_BY_N_LEADING_CHARACTERS.getRegexp(COUNT_OF_SYMBOLS_IN_LOG),
                        ""));
        return response;
    }
}
