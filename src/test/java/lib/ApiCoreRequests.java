package lib;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiCoreRequests {
    @Step("GЕT-запрос c выводом статус кода ответа")
    public Response makeGetRequestStatusCode(String url) {
        Response response = given()
                .filter(new AllureRestAssured())
                .get(url)
                .andReturn();
        System.out.println("Статус код: " + response.statusCode());
        return response;
    }
    @Step("GЕT-запрос")
    public Response makeGetRequest(String url) {
        return given()
                .filter(new AllureRestAssured())
                .get(url)
                .andReturn();
    }

    @Step("POST-запрос")
    public Response makePostRquest(String url, Map<String, String> authData) {
        return given()
                .filter(new AllureRestAssured())
                .body(authData)
                //.log().all()
                .post(url)
                .andReturn();

    }
@Step("GET-запрос с token и cookie")
    public Response makeGetRquestDetails(String url, String token, String cookie) {
    return given()
            .filter(new AllureRestAssured())
            .header(new Header("x-csrf-token", token))
            .cookie("auth_sid", cookie)
            .get(url)
            .andReturn();
}
@Step("GET-запрос только с cookie")
    public Response makeGetRquestWithCookie(String url, String cookie) {
    return given()
            .filter(new AllureRestAssured())
            .cookie("auth_sid", cookie)
            .get(url)
            .andReturn();
}
@Step("GET-запрос только с token")
    public Response makeGetRquestWithToken(String url, String token) {
    return given()
            .filter(new AllureRestAssured())
            .header(new Header("x-csrf-token", token))
            .get(url)
            .andReturn();
}
}
