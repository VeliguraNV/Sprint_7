import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginTest {

    private final String BASE_URL = "https://qa-scooter.praktikum-services.ru";
    private final String COURIER_LOGIN_ENDPOINT = "/api/v1/courier/login";
    private String courierLogin;
    private String courierPassword;

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Before
    public void createTestCourier() {
        courierLogin = "testcourier" + System.currentTimeMillis();;
        courierPassword = "1234";

        String courierCreateEndpoint = "/api/v1/courier";
        String requestBody = String.format("{\"login\": \"%s\", \"password\": \"%s\", \"firstName\": \"Test\"}", courierLogin, courierPassword);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(courierCreateEndpoint)
                .then()
                .extract()
                .response();

        if (response.statusCode() == 409) { // Courier already exists
            System.out.println("Courier already exists.");
        } else {
            response.then().statusCode(201);
        }
    }

    @After
    public void deleteTestCourier() {
        int courierId = getCourierId(courierLogin, courierPassword);
        String courierDeleteEndpoint = "/api/v1/courier/" + courierId;

        given()
                .when()
                .delete(courierDeleteEndpoint)
                .then()
                .statusCode(200);
    }

    @Step("Get courier ID")
    private int getCourierId(String login, String password) {
        String requestBody = String.format("{\"login\": \"%s\", \"password\": \"%s\"}", login, password);
        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(COURIER_LOGIN_ENDPOINT)
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .extract()
                .response();

        return response.path("id");
    }

    @Test
    @Step("Courier can log in")
    public void courierCanLogIn() {
        String requestBody = String.format("{\"login\": \"%s\", \"password\": \"%s\"}", courierLogin, courierPassword);

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(COURIER_LOGIN_ENDPOINT)
                .then()
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    @Step("Missing mandatory fields")
    public void missingMandatoryFields() {
        String requestBody = String.format("{\"login\": \"%s\"}", courierLogin);

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(COURIER_LOGIN_ENDPOINT)
                .then()
                .statusCode(504);

        requestBody = String.format("{\"password\": \"%s\"}", courierPassword);

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(COURIER_LOGIN_ENDPOINT)
                .then()
                .statusCode(504);
    }

    @Test
    @Step("Incorrect login or password")
    public void incorrectLoginOrPassword() {
        String requestBody = String.format("{\"login\": \"%s\", \"password\": \"%s\"}", "wrongLogin", "wrongPassword");

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(COURIER_LOGIN_ENDPOINT)
                .then()
                .statusCode(404);
    }

    @Test
    @Step("Log in with non-existent user")
    public void loginWithNonExistentUser() {
        String requestBody = String.format("{\"login\": \"%s\", \"password\": \"%s\"}", "nonexistentuser", "password");

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(COURIER_LOGIN_ENDPOINT)
                .then()
                .statusCode(404);
    }
}