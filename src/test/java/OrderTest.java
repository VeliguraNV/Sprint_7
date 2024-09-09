import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderTest {

    private final String CREATE_ORDER_ENDPOINT = "/api/v1/orders";
    // private final String GET_ORDERS_ENDPOINT = "/api/v1/orders";
    private final String COURIER_CREATE_ENDPOINT = "/api/v1/courier";
    private final String COURIER_LOGIN_ENDPOINT = "/api/v1/courier/login";
    private final String COURIER_DELETE_ENDPOINT_BASE = "/api/v1/courier/";

    private String[] colors;
    private String courierLogin;
    private String courierPassword;
    private int courierId;

    public OrderTest(String[] colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{"BLACK", "GREY"}},
                {new String[]{}}
        });
    }

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Before
    @Step("Create a courier for testing")
    public void createTestCourier() {
        courierLogin = "testcourier" + System.currentTimeMillis();
        courierPassword = "1234";

        String requestBody = String.format("{\"login\": \"%s\", \"password\": \"%s\", \"firstName\": \"Test\"}", courierLogin, courierPassword);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(COURIER_CREATE_ENDPOINT)
                .then()
                .statusCode(201)
                .extract()
                .response();

        courierId = getCourierId(courierLogin, courierPassword);
    }

    @After
    @Step("Delete the test courier")
    public void deleteTestCourier() {
        given()
                .when()
                .delete(COURIER_DELETE_ENDPOINT_BASE + courierId)
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
    @Step("Create order with colors: {0}")
    public void createOrder() {
        String requestBody = String.format("{\"firstName\": \"Иван\", \"lastName\": \"Иванов\", \"address\": \"ул. Пушкина, д. Колотушкина\", \"metroStation\": \"4\", \"phone\": \"+7 800 555 35 35\", \"rentTime\": \"5\", \"deliveryDate\": \"2023-10-10\", \"comment\": \"Нет комментариев\", \"color\": %s}", Arrays.toString(colors));

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(CREATE_ORDER_ENDPOINT)
                .then()
                .statusCode(201)
                .body("track", notNullValue());
    }

}
