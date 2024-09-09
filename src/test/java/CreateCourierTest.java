import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CreateCourierTest {


    private String login;
    private String password;
    private String firstName;

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Before
    public void generateTestData() {
        login = "testCourier_" + System.currentTimeMillis();
        password = "password123";
        firstName = "John";
    }


    @Test
    @Step("Success Creating Courier on route /api/v1/courier ")
    public void testCreateCourier() {
        createCourier(login, password, firstName)
                .then()
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @Test
    @Step("unavailability Creating duplicate Courier on route /api/v1/courier ")
    public void testCannotCreateDuplicateCouriers() {
        createCourier(login, password, firstName);
        createCourier(login, password, firstName)
                .then()
                .statusCode(409);
    }

    @Test
    @Step("Create courier without password on route /api/v1/courier ")
    public void testCreateCourierRequiresMandatoryFields() {
        given()
                .header("Content-Type", "application/json")
                .body(new HashMap<String, String>() {{
                    put("password", password);
                    put("firstName", firstName);
                }})
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(400);
    }

    @Step("Создать курьера с логином {login}, паролем {password} и именем {firstName}")
    private Response createCourier(String login, String password, String firstName) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("login", login);
        requestBody.put("password", password);
        requestBody.put("firstName", firstName);

        return given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/courier");
    }

    @Test
    @Step("Missing Field Returns Error")
    public void testMissingFieldReturnsError() {
        given()
                .header("Content-Type", "application/json")
                .body(login)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(400);
    }

    @Test
    @Step("Existing Login Returns Error")
    public void testExistingLoginReturnsError() {
        createCourier(login, password, firstName);

        given()
                .header("Content-Type", "application/json")
                .body(new HashMap<String, String>() {{
                    put("login", login);
                    put("password", password);
                    put("firstName", firstName);
                }})
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(409);
    }
}

