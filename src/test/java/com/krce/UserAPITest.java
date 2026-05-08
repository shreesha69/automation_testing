package com.krce;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

public class UserAPITest {

    private String name;
    private String email;
    private String password;
    private String accessToken;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api.escuelajs.co/api/v1";
    }

    @Test(priority = 1)
    public void testRegister() {
        name     = "user_" + System.currentTimeMillis();
        email    = name + "@gmail.com";
        password = "12345678910";
        String avatar = "https://picsum.photos/800";

        Map<String, String> body = Map.of(
                "name",     name,
                "email",    email,
                "password", password,
                "avatar",   avatar
        );

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/users/")
                .then()
                .log().all()
                .statusCode(201)
                .body("id",   Matchers.notNullValue())
                .body("name", Matchers.equalTo(name));
    }

    @Test(priority = 2)
    public void testLogin() {
        Map<String, String> body = Map.of(
                "email",    email,
                "password", password
        );

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/auth/login");

        response
                .then()
                .log().all()
                .statusCode(201)
                .body("access_token",  Matchers.notNullValue())
                .body("refresh_token", Matchers.notNullValue());

        accessToken = response.jsonPath().getString("access_token");
        System.out.println("Access Token: " + accessToken);
    }

    @Test(priority = 3)
    public void testGetProfile() {
        RestAssured.given()
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .get("/auth/profile")
                .then()
                .log().all()
                .statusCode(200)
                .body("id",    Matchers.notNullValue())
                .body("email", Matchers.equalTo(email))
                .body("name",  Matchers.equalTo(name));
    }
}