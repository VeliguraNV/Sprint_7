import client.Courier;
import client.Credentials;
import client.ScooterServiceClient;
import io.qameta.allure.Step;

import io.restassured.response.ValidatableResponse;
import org.junit.*;

import static org.hamcrest.Matchers.*;

public class CourierLoginTest {

    private ScooterServiceClient client = new ScooterServiceClient();

    private static final String COURIER_LOGIN_ENDPOINT = "/api/v1/courier/login";
    private static String request_body;
    private Courier courier;
    private String courierID;

    @Before
    public void before() {
        courier = new Courier("new_courier_" + System.currentTimeMillis(), "password", "Name");
        client.createCourier(courier);
    }

    @After
    public void after() {
        if (courierID != null) {
            ValidatableResponse response = client.loginCourier(Credentials.fromCourier(courier));
            client.deleteCourier(courierID);
            response.assertThat().statusCode(200);
        }
    }

    @Test
    @Step("Success login")
    public void courierLogin() {
        ValidatableResponse response = client.loginCourier(Credentials.fromCourier(courier));
        response.assertThat().statusCode(200);
        courierID = response.extract().jsonPath().getString("id");
    }

    @Test
    @Step("Login with incorrect password")
    public void courierLoginIncorrectPassword() {
        Courier wrongPasswordCourier = new Courier(courier.getLogin(), "wrongPassword", courier.getFirstName());
        ValidatableResponse response = client.loginCourier(Credentials.fromCourier(wrongPasswordCourier));
        response.assertThat().statusCode(404)
                .and().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @Step("Login with missing fields")
    public void courierLoginMissingFields() {
        Courier missingLoginCourier = new Courier(null, "password", "name");
        ValidatableResponse response = client.loginCourier(Credentials.fromCourier(missingLoginCourier));
        response.assertThat().statusCode(400)
                .and().body("message", equalTo("Недостаточно данных для входа"));

    }
}
