import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import io.restassured.http.Headers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestHeader {
    @Test
    public void testHeader(){
        Response responseGetheader = RestAssured
                .post("https://playground.learnqa.ru/api/homework_header")
                .andReturn();

        Headers headers = responseGetheader.getHeaders();
        assertTrue(headers.hasHeaderWithName("x-secret-homework-header"), "There is no such header");
        String value = headers.getValue("x-secret-homework-header");
        assertEquals("Some secret value", value);
    }
}
