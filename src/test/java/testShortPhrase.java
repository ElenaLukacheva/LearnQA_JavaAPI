import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class testShortPhrase {
    @ParameterizedTest
    @ValueSource(strings = {"привет", "resstassured the best", "пуир", ""})
    public void testShortPhrase(String name){

        assertTrue(name.length() > 15);
    }}
