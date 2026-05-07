package com.krce;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

public class UserAPITest {

    int id;
    String email;
    String name;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api.escuelajs.co/api/v1";
    }

    @Test(priority = 1)
    public void testCreateUser() {
        name = "user_" + System.currentTimeMillis();
        email = "user_" + System.currentTimeMillis() + "@gmail.com";

        Map<String, String> body = Map.of(
                "name", name,
                "email", email,
                "password", "1234",
                "avatar", "https://picsum.photos/800"
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
                .body("name", Matchers.equalTo(name))
                .body("email", Matchers.equalTo(email))
                .body("role", Matchers.equalTo("customer"));

        id = response.jsonPath().getInt("id");
        System.out.println("Created User ID: " + id);
    }

    @Test(priority = 2)
    public void testGetUser() {
        RestAssured.given()
                .pathParam("id", id)
                .when()
                .get("/users/{id}")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", Matchers.equalTo(id))
                .body("name", Matchers.equalTo(name))
                .body("email", Matchers.equalTo(email));
    }

    @Test(priority = 3)
    public void testUpdateUser() {
        String updatedName = "updated_" + System.currentTimeMillis();
        String updatedEmail = "updated_" + System.currentTimeMillis() + "@gmail.com";

        Map<String, String> body = Map.of(
                "name", updatedName,
                "email", updatedEmail
        );

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .pathParam("id", id)
                .body(body)
                .when()
                .put("/users/{id}");

        response
                .then()
                .log().all()
                .statusCode(200)
                .body("id", Matchers.equalTo(id))
                .body("name", Matchers.equalTo(updatedName))
                .body("email", Matchers.equalTo(updatedEmail));

        name = updatedName;
        email = updatedEmail;
        System.out.println("Updated User ID: " + id);
    }

    @Test(priority = 4)
    public void testDeleteUser() {
        RestAssured.given()
                .pathParam("id", id)
                .when()
                .delete("/users/{id}")
                .then()
                .log().all()
                .statusCode(200);

        System.out.println("Deleted User ID: " + id);
    }

}