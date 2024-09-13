package client;

import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class ScooterServiceClient {
    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";
    public ValidatableResponse createCourier(Courier courier){
      return  given()
                .log()
                .all()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .body(courier)
                .post("/api/v1/courier")
                        .then()
                        .log()
                        .all();
    }

    public ValidatableResponse loginCourier(Credentials credentials){
        return  given()
                .log()
                .all()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .body(credentials)
                .post("/api/v1/courier/login")
                .then()
                .log()
                .all();
    }
public void deleteCourier(String courierID){
    given()
            .log()
            .all()
            .baseUri(BASE_URL)
            .header("Content-type", "application/json")
            .body("{\n" +
                    "    \"id\":\"" + courierID + "\"\n" +
                    "}")
            .delete("/api/v1/courier/:id")
            .then()
            .log()
            .all();
}

    }

