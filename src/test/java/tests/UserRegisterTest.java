package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
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

import static io.restassured.RestAssured.given;
import static lib.DataGenerate.getRegistrationData;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.in;

public class UserRegisterTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    @Epic("Тесты на создание пользователя")
    @Feature("Создание пользователя")

    @Test
    @Description("")
    @DisplayName("Негатиный тест на создание пользователя с некорректным email")
    public void testCreatUserWithIncorrectEmail() {
        String email = "vinkotovexample.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = getRegistrationData(userData);

        Response responseIncorrectEmail = apiCoreRequests
                .makePostRquest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodeEquals(responseIncorrectEmail, 400);

    }
    @CsvSource({
                 "email",
                "password",
                "username",
                "firstName",
                "lastName"
        })
    @ParameterizedTest
    @DisplayName("Негатиный тест на Создание пользователя без указания одного из полей")
    public void testCreatUserWithIncorrectEmail(String missingField) {
            Map<String, String> nonDefaultValues = new HashMap<>();
            nonDefaultValues.put(missingField, "");

            Map<String, String> userData = getRegistrationData(nonDefaultValues);
            Response requestCheckCreatedUser = apiCoreRequests
                    .makePostRquest("https://playground.learnqa.ru/api/user/", userData);

            Assertions.assertResponseCodeEquals(requestCheckCreatedUser, 400);
        }

    @Test
    @DisplayName("Негативный тест на Создание пользователя с очень коротким именем")
    public void testShortName(){
        String username = "K";
        Map<String, String> userData = new HashMap<>();
        userData.put("username", username);
        userData = getRegistrationData(userData);

      Response requestCheckCreatedUser = apiCoreRequests
              .makePostRquest("https://playground.learnqa.ru/api/user/", userData);

      Assertions.assertResponseCodeEquals(requestCheckCreatedUser, 400);

    }

    @Test
    @DisplayName("Негативный тест на создание пользователя с очень длинным именем")
    public void testLongName(){
        String username = "Яркая осень среди золотых листьев и прохладного ветра. Воздух пронизывает свежестью, напоминая о скором наступлении зимы. На улицах города шум и суета, а в душе тишина и спокойствие. В каждом уголке чувствуется невидимая магия, пробуждающая в сердце радость и надежду. В это время года особенно приятно погрузиться в мир книги, отдаваясь волшебству слов и сюжетов, забывая обо всем на свете.";

        Map<String, String> userData = new HashMap<>();
        userData.put("username", username);
        userData = getRegistrationData(userData);

        Response requestCheckCreatedUser = apiCoreRequests
                .makePostRquest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodeEquals(requestCheckCreatedUser, 400);
    }
    @Test
    public void testCreateUserWithExistingEmail() {
        String email = "vinkotov@example.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = getRegistrationData(userData);

        Response responseCreateAuth = given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");
    }
   @Test
    public void testCreateUserSuccessefully() {
        String email = DataGenerate.getRandomEmail();

        Map<String, String> userData = getRegistrationData();

        Response responseCreateAuth = given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
        Assertions.assertJsonHasField(responseCreateAuth, "id");
    }
}
