package lib;

import io.restassured.http.Headers;
import io.restassured.response.Response;

import java.util.Map;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseTestCase {
    protected String getHeader(Response Response, String name) {
        Headers headers = Response.getHeaders();

        assertTrue(headers.hasHeaderWithName(name), "response doesn't have header with name" + name);
        return headers.getValue(name);
    }

    protected String getCookie (Response Response, String name) {
            Map<String, String> cookies = Response.getCookies();

            assertTrue(cookies.containsKey(name), "response doesn't have cookie with " + name);
            return cookies.get(name);
        }

        protected int getInFromJson(Response Response, String name) {
            Response.then().assertThat().body("$", hasKey(name));
            return Response.jsonPath().getInt(name);
        }
    }

