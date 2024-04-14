import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
public class Long_redirect_2 {

    @Test
    public void testRequestTrackingRedirects() {
        String url = "https://playground.learnqa.ru/api/long_redirect";
        int redurects = 0;

        while (true) {
            Response response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .when()
                    .get(url)
                    .andReturn();

            redurects++;

            int statusCode = response.getStatusCode();

            if (statusCode == 301 || statusCode == 302 || statusCode == 303) {
                url = response.getHeader("Location");
            } else if (statusCode == 200) {
                System.out.println("Количество редиректов: " + redurects);
                break;
            }
        }
    }
 }

