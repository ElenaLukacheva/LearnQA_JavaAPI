
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class Longtime_job {
    @Test
    public void testLongJob() throws InterruptedException {
         String url = "https://playground.learnqa.ru/ajax/api/longtime_job";

        JsonPath jsonPath = RestAssured
                .given()
                .get(url)
                .jsonPath();

        String token = jsonPath.get("token");
        int seconds = jsonPath.getInt("seconds");
            seconds = (seconds) * 1000;

        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        JsonPath response = RestAssured
                .given()
                .queryParams(params)
                .get(url)
                .jsonPath();
        String status = response.get("status");

        Assertions.assertEquals("Job is NOT ready", status);

       sleep(seconds);

        JsonPath response2 = RestAssured
                .given()
                .queryParams(params)
                .get(url)
                .jsonPath();

        String status2 = response2.get("status");
        Assertions.assertEquals("Job is ready", status2);
        String result = response2.get("result");
        assertNotNull(result);
    }
}