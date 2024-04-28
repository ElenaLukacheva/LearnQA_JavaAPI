import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestCookie {
    @Test
    public void testCookie() {
        Map<String, String> authData = new HashMap<>();
        authData.put("login", "elena@example.com");
        authData.put("password", "elena");

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();

        Map<String, String> cookies = responseGetAuth.getCookies();
        System.out.println(cookies);
        assertTrue(cookies.containsKey("HomeWork"), "Key does not match");
        assertTrue(cookies.containsValue("hw_value"), "Value does not match");
    }
}
