import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

public class Check_auth_cookie {
    @Test
    public void testPassword() {
        String[] variableValues = {"password", "123456", "12345678", "qwerty", "abc123", "monkey", "1234567", "letmein", "trustno1", "1dragon", "baseball", "111111", "iloveyou", "master", "sunshine", "ashley", "bailey", "passw0rd", "shadow", "123123", "654321", "superman", "qazwsx", "dragon", "welcome", "football", "jesus", "michael", "ninja", "Football", "123456789", "adobe123[a]", "admin", "1234567890", "photoshop[a]", "1234", "12345", "password1", "princess", "azerty", "mustang", "access", "696969", "1qaz2wsx", "login", "qwertyuiop", "solo", "batman", "121212", "flower", "hottie", "loveme", "starwars", "hello", "freedom", "whatever", "zaq1zaq1", "666666", "!@#$%^&*", "charlie", "aa123456", "donald", "qwerty123", "1q2w3e4r", "555555", "lovely", "7777777", "888888"};
        for (String pass : variableValues) {
            Map<String, String> data = new HashMap<>();
            data.put("login", "super_admin");
            data.put("password", pass);

            Response responseForCookie = RestAssured
                    .given()
                    .body(data)
                    .when()
                    .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                    .andReturn();
            String responseCookie = responseForCookie.getCookie("auth_cookie");

            Map<String, String> cookies = new HashMap<>();
            cookies.put("auth_cookie", responseCookie);

            Response responseForCheck = RestAssured
                    .given()
                    .body(data)
                    .cookies(cookies)
                    .when()
                    .post("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                    .andReturn();

            String responseBody = responseForCheck.getBody().asString();

            if (responseBody.contains("You are authorized")) {
                System.out.println("Успешная авторизация с паролем: " + pass);
            } else {
                System.out.println("Неудачная авторизация с паролем: " + pass);
            }
        }
    }
}