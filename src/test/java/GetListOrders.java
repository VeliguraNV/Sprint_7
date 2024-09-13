import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class GetListOrders {
    @Before
        public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
@Step("Get list of orders")
public void getListOfOrders() {
    given()
            .when()
            .get("/api/v1/orders")
            .then()
            .statusCode(200)
            .body("orders", notNullValue());
}
}

