package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.Map;

public class UserEditTest extends BaseTestCase {
    String cookie;
    String header;
    int userId;
    ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    @Epic("Тесты на изменение пользователя")

    @CsvSource({
            "email, vinkotovexample.com,",
            "firstName, D"
    })
    @ParameterizedTest
    @Description("Попытаемся изменить email пользователя, будучи авторизованными тем же пользователем, на новый email без символа @ и " +
                 "Попытаемся изменить firstName пользователя, будучи авторизованными тем же пользователем, на очень короткое значение в один символ")

    @DisplayName("Негативный тесты на изменение пользователя")
    public void testNegativeChangeUserShortName(String val, String val2){
        //генерация и создание
        Map<String, String> userData = DataGenerate.getRegistrationData();

        Response responseCreate = apiCoreRequests
                .makePostRquest("https://playground.learnqa.ru/api/user/", userData);

        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        //логин
        Response requestLogin = apiCoreRequests
                .makePostRquest("https://playground.learnqa.ru/api/user/login", authData);
        this.cookie = this.getCookie(requestLogin,"auth_sid");
        this.header = this.getHeader(requestLogin,"x-csrf-token");
        this.userId = this.getInFromJson(requestLogin,"user_id");

        //изменение пользователя
        authData.put(val, val2);
        Response responsePut = apiCoreRequests
                .makePutRequestDetails(
                        "https://playground.learnqa.ru/api/user/" + this.userId,
                        authData,
                        this.header,
                        this.cookie
                );

        Assertions.assertResponseCodeEquals(responsePut, 400);
        Assertions.assertJsonHasField(responsePut, "error");
    }

    @Test
    @Description("Попытаемся изменить email пользователя, будучи авторизованными тем же пользователем, на новый email без символа @")
    @DisplayName("Негативный тест на изменение пользователя c некорректным email")
    public void testNegativeChangeUser(){
        //генерация и создание
        Map<String, String> userData = DataGenerate.getRegistrationData();

        Response responseCreate = apiCoreRequests
                .makePostRquest("https://playground.learnqa.ru/api/user/", userData);

        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        //логин
        Response requestLogin = apiCoreRequests
                .makePostRquest("https://playground.learnqa.ru/api/user/login", authData);
                this.cookie = this.getCookie(requestLogin,"auth_sid");
                this.header = this.getHeader(requestLogin,"x-csrf-token");
                this.userId = this.getInFromJson(requestLogin,"user_id");

        //изменение пользователя
        authData.put("email", "vinkotovexample.com");
        Response responsePut = apiCoreRequests
                .makePutRequestDetails(
                        "https://playground.learnqa.ru/api/user/" + this.userId,
                        authData,
                        this.header,
                        this.cookie
                );

        Assertions.assertResponseCodeEquals(responsePut, 400);
        Assertions.assertJsonHasField(responsePut, "error");
    }

    @Test
    @DisplayName("Тест на изменение данных пользователя, будучи авторизованными другим пользователем")
    public  void testChangeUserWithAuthOthersUser(){

        Map<String, String> userData = new HashMap<>();
        userData.put("email", "vinkotov@example.com");
        userData.put("password", "1234");

        Response response = apiCoreRequests
                .makePostRquest("https://playground.learnqa.ru/api/user/login", userData);

        this.cookie = this.getCookie(response,"auth_sid");
        this.header = this.getHeader(response, "x-csrf-token");

        userData.put("email", "vinfhfgdgv@ghfgd.com");
        System.out.println(userData);
        Response responseGetUser = apiCoreRequests
                .makePutRequestDetails(
                        "https://playground.learnqa.ru/api/user/96572",
                        userData,
                        this.header,
                        this.cookie
                        );
        Assertions.assertResponseCodeEquals(responseGetUser, 400);

    }

    @Test
    @DisplayName("Изменение юзера, будучи не авторизованным")
        public void testChangeUserNotAuth(){
        Map<String, String> userData = new HashMap<>();
        userData.put("firstName", "Ivan");

        Response response = apiCoreRequests
                .makePutRequest("https://playground.learnqa.ru/api/user/96572", userData);

        Assertions.assertResponseCodeEquals(response, 400);
    }



    @Test
    public void addedJustCreatedTest() {
        //generate user
        Map<String, String> userData = DataGenerate.getRegistrationData();

        JsonPath responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .jsonPath();

        String userId = responseCreateAuth.getString("id");

        //login
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        //edit
        String newName = "Changet Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth, "auth_sid"))
                .body(editData)
                .put("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();

        //get
        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth, "auth_sid"))
                .get("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();

        Assertions.assertJsonByName(responseUserData, "firstName", newName);
    }
}