package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.BaseTestCase;
import lib.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserGetTest extends BaseTestCase {
    String cookie;
    String header;
    int userId;
   private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @DisplayName("Тест на получение данных другого юзера")
    public void testUserDataAnotherUser(){
    Map<String, String> userData = new HashMap<>();
    userData.put("email","vinkotov@example.com" );
    userData.put("password", "1234");

        Response responseAuth = apiCoreRequests
                .makePostRquest("https://playground.learnqa.ru/api/user/login", userData);
        this.cookie = this.getCookie(responseAuth,"auth_sid");
        this.header = this.getHeader(responseAuth, "x-csrf-token");

    Response responseAuthAnotherUser = apiCoreRequests
            .makeGetRquestDetails(
                    "https://playground.learnqa.ru/api/user/96572",
                    this.header,
                    this.cookie
            );

    Assertions.assertJsonHasField(responseAuthAnotherUser, "username");
    Assertions.assertJsonHasNoField(responseAuthAnotherUser, "firstName");
    Assertions.assertJsonHasNoField(responseAuthAnotherUser, "lastName");
    Assertions.assertJsonHasNoField(responseAuthAnotherUser, "email");

    }

    @Test
    public void testGetUserdataNotAuth(){
        Response responseUserData = RestAssured
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();

        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasNoField(responseUserData, "firstName");
        Assertions.assertJsonHasNoField(responseUserData, "lastName");
        Assertions.assertJsonHasNoField(responseUserData, "email");
    }
@Test
    public void testGetUserdatailsAuthAsSomeUser(){
    Map<String, String> authData = new HashMap<>();
    authData.put("email", "vinkotov@example.com");
    authData.put("password", "1234");

    Response responseGetAuth = RestAssured
            .given()
            .body(authData)
            .post("https://playground.learnqa.ru/api/user/login")
            .andReturn();
    String header = this.getHeader(responseGetAuth, "x-csrf-token");
    String cookie = this.getCookie(responseGetAuth, "auth_sid");

    Response responseUserData = RestAssured
            .given()
            .header("x-csrf-token", header)
            .cookie("auth_sid", cookie)
            .get("https://playground.learnqa.ru/api/user/2")
            .andReturn();
String[] expectedFields = {"username", "firstName", "lastName","email" };
}

}
