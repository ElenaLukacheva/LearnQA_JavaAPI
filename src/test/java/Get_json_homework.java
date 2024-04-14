import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class Get_json_homework {

    @Test
    public void testGetJsonHomework() {
        JsonPath response = RestAssured
                .given()
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();
        ArrayList name = response.get("messages");
        System.out.println(name.get(1));

    }

}


