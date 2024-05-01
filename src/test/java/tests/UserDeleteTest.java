package tests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserDeleteTest extends BaseTestCase {
    ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    String cookie;
    String header;
    int userIdOnAuth;
    @Epic("Тесты на удаление пользователя")
    @Feature("Удаление пользователя")
    @Owner("Elena Lukacheva")
    @Issue("Ex19: Теги Allure")
    @Severity(SeverityLevel.BLOCKER)
    @Test
    @DisplayName("Удалить пользователя по ID 2")
    public void testDeleteUserId2(){
        Map<String, String> data = new HashMap<>();
        data.put("email", "vinkotov@example.com");
        data.put("password", "1234");

        Response responseAuth = apiCoreRequests
                .makePostRquest("https://playground.learnqa.ru/api/user/login", data);

        this.header = this.getHeader(responseAuth, "x-csrf-token");
        this.cookie = this.getCookie(responseAuth, "auth_sid");
        this.userIdOnAuth = this.getInFromJson(responseAuth, "user_id");

        Response requestDel = apiCoreRequests
                .makeDeletRequestWithAuth(
                        "https://playground.learnqa.ru/api/user/",
                        userIdOnAuth,
                        header,
                        cookie);

        Assertions.assertResponseCodeEquals(requestDel, 400);
        Assertions.assertJsonHasField(requestDel, "error");
    }
    @Test
    @Owner("Elena Lukacheva")
    @Issue("Ex19: Теги Allure")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Создать пользователя авторизоваться из-под него удалить затем попробовать получить его данные по ID и убедитьсячто пользователь действительно удален")
    public void testDeleteUser(){
        //генерация и создание
        Map<String, String> data = DataGenerate.getRegistrationData();

        Response responseCreate = apiCoreRequests
                .makePostRquest("https://playground.learnqa.ru/api/user/", data);

        Response responseAuth = apiCoreRequests
                .makePostRquest("https://playground.learnqa.ru/api/user/login", data);

        this.header = this.getHeader(responseAuth, "x-csrf-token");
        this.cookie = this.getCookie(responseAuth, "auth_sid");
        this.userIdOnAuth = this.getInFromJson(responseAuth, "user_id");

        Response responseDel = apiCoreRequests
                .makeDeletRequestWithAuth(
                        "https://playground.learnqa.ru/api/user/",
                        this.userIdOnAuth,
                        this.header,
                        this.cookie
                );
        Response responseGet = apiCoreRequests
                .makeGetRquestDetails(
                        "https://playground.learnqa.ru/api/user/" + userIdOnAuth,
                        this.cookie,
                        this.header
        );
        Assertions.assertResponseCodeEquals(responseGet, 404);
        Assertions.assertResponseTextEquals(responseGet,"User not found");
    }
    @Test
    @Owner("Elena Lukacheva")
    @Issue("Ex19: Теги Allure")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Попробовать удалить пользователя, будучи авторизованными другим пользователем")
    public void testDelUserNoAuth(){
        Map<String, String> newUser = new HashMap<>();
        newUser = DataGenerate.getRegistrationData();

        Response respCreate = apiCoreRequests
                .makePostRquest("https://playground.learnqa.ru/api/user/", newUser);

        this.userIdOnAuth = this.getInFromJson(respCreate, "id");

        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseAuth = apiCoreRequests
                .makePostRquestAuth(authData);
        this.cookie = this.getCookie(responseAuth, "auth_sid");
        this.header = this.getHeader(responseAuth, "x-csrf-token");

        Response responseDel2 = apiCoreRequests
                .makeDeletRequestWithAuth(
                        "https://playground.learnqa.ru/api/user/",
                        this.userIdOnAuth,
                        this.cookie,
                        this.header
                );
        Assertions.assertResponseCodeEquals(responseDel2, 400);
        Assertions.assertResponseTextEqualsError(responseDel2,"Auth token not supplied");
    }
}
