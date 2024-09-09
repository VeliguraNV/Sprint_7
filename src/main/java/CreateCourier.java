import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.*;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CreateCourier {

    private String login;
    private String password;
    private String firstName;
}

//    @BeforeClass
//    public static void setup() {
//        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
//    }
//
//    @Before
//    public void generateTestData() {
//        login = "testCourier_" + System.currentTimeMillis();
//        password = "password123";
//        firstName = "John";
//    }
//
//    @After
//    public void deleteTestData() {
//        // Добавьте код для удаления тестовых данных (курьера)
//    }
//
//    @Test
//    public void testCreateCourier() {
//        createCourier(login, password, firstName)
//                .then()
//                .statusCode(201)
//                .body("ok", equalTo(true));
//    }
//
//    @Test
//    public void testCannotCreateDuplicateCouriers() {
//        createCourier(login, password, firstName);
//        createCourier(login, password, firstName)
//                .then()
//                .statusCode(409);
//    }
//
//    @Test
//    public void testCreateCourierRequiresMandatoryFields() {
//        given()
//                .header("Content-Type", "application/json")
//                .body(new HashMap<String, String>() {{
//                    put("password", password);
//                    put("firstName", firstName);
//                }})
//                .when()
//                .post("/api/v1/courier")
//                .then()
//                .statusCode(400);
//    }
//
//    @Step("Создать курьера с логином {login}, паролем {password} и именем {firstName}")
//    private Response createCourier(String login, String password, String firstName) {
//        Map<String, String> requestBody = new HashMap<>();
//        requestBody.put("login", login);
//        requestBody.put("password", password);
//        requestBody.put("firstName", firstName);
//
//        return given()
//                .header("Content-Type", "application/json")
//                .body(requestBody)
//                .when()
//                .post("/api/v1/courier");
//    }
//
//    @Test
//    public void testMissingFieldReturnsError() {
//        given()
//                .header("Content-Type", "application/json")
//                .body(new HashMap<String, String>() {{
//                    put("login", login);
//                    }})
//                .when()
//                .post("/api/v1/courier")
//                .then()
//                .statusCode(400);
//    }
//
//    @Test
//    public void testExistingLoginReturnsError() {
//        createCourier(login, password, firstName);
//
//        given()
//                .header("Content-Type", "application/json")
//                .body(new HashMap<String, String>() {{
//                    put("login", login);
//                    put("password", password);
//                    put("firstName", firstName);
//                }})
//                .when()
//                .post("/api/v1/courier")
//                .then()
//                .statusCode(409);
//    }
//}