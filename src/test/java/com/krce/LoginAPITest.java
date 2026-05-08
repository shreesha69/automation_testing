package com.krce;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

public class LoginAPITest {

    String email;
    String password;
    int id;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api.escuelajs.co/api/v1";
      //  RestAssured.useRelaxedHTTPSValidation();
    }

    @Test(priority = 1)
    public void testRegister() {
        String name   = "user_" + System.currentTimeMillis();
        email         = name + "@gmail.com";
        password      = "1234567890";
        String avatar = "https://picsum.photos/800";

        Map<String, String> body = Map.of(
                "name",     name,
                "email",    email,
                "password", password,
                "avatar",   avatar
        );

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/users/");

        response
                .then()
                .log().all()
                .statusCode(201)
                .body("id", Matchers.notNullValue());

        id = response.jsonPath().getInt("id");
        System.out.println("Registered User ID: " + id);
    }
}