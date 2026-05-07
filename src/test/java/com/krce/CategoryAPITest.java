package com.krce;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

public class CategoryAPITest {

    int id;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api.escuelajs.co/api/v1";
    }

    @Test(priority = 1)
    public void testCreateCategory() {
        String name  = "category_" + System.currentTimeMillis();
        String image = "https://google.com";

        Map<String, String> body = Map.of(
                "name",  name,
                "image", image
        );

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/categories");

        response
                .then()
                .log().all()
                .statusCode(201)
                .body("name", Matchers.equalTo(name));

        id = response.jsonPath().getInt("id");
        System.out.println("Created Category ID: " + id);
    }

    @Test(priority = 2)
    public void testGetCategory() {
        RestAssured.given()
                .pathParam("id", id)
                .when()
                .get("/categories/{id}")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", Matchers.equalTo(id));
    }

    @Test(priority = 3)
    public void testUpdateCategory() {
        String updatedName = "updated_" + System.currentTimeMillis();

        Map<String, String> body = Map.of(
                "name",  updatedName,
                "image", "https://google.com"
        );

        RestAssured.given()
                .contentType(ContentType.JSON)
                .pathParam("id", id)
                .body(body)
                .when()
                .put("/categories/{id}")
                .then()
                .log().all()
                .statusCode(200)
                .body("id",   Matchers.equalTo(id))
                .body("name", Matchers.equalTo(updatedName));
    }
    @Test(priority = 4)
    public void testDeleteCategory() {
        RestAssured.given()
                .pathParam("id", id)
                .when()
                .delete("/categories/{id}")
                .then()
                .log().all()
                .statusCode(200);
    }
}