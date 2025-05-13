package com.annasozonova.api.tests;

import com.annasozonova.api.base.BaseApiTest;
import com.annasozonova.api.dto.CreateDashboardRequest;
import io.qameta.allure.*;
import io.qameta.allure.junit5.AllureJunit5;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * API test suite for managing dashboards in Report Portal.
 */
@Epic("API Tests")
@Feature("Dashboard Management")
@ExtendWith(AllureJunit5.class)
public class DashboardTests extends BaseApiTest {

    private static final Logger log = LoggerFactory.getLogger(DashboardTests.class);

    @Test
    @Story("Positive: Create new dashboard")
    @DisplayName("Create a new dashboard via API")
    @Description("Verifies that a dashboard is created successfully when a valid request body is provided, and that it is accessible by ID")
    void createDashboard_shouldReturn201_andBeAccessibleById() {
        String name = "TestDashboard_" + UUID.randomUUID();
        CreateDashboardRequest request = new CreateDashboardRequest(name, "from API", false);

        log.info("Creating dashboard: {}", name);
        int id = createDashboard(request);

        log.info("Verifying dashboard by ID: {}", id);
        verifyDashboardExistsById(id, name);
    }

    @Test
    @Story("Negative: Create dashboard without required fields")
    @DisplayName("Attempt to create dashboard without 'name' field")
    @Description("Verifies that the API returns 400 Bad Request when the required 'name' field is missing")
    void createDashboard_withoutName_shouldReturn400() {
        log.info("Validation test: creating dashboard with missing 'name' field");
        CreateDashboardRequest invalidRequest = new CreateDashboardRequest(null, "missing name", false);

        given()
                .filter(new AllureRestAssured())
                .auth().oauth2(TOKEN)
                .contentType(ContentType.JSON)
                .body(invalidRequest)
                .when()
                .post("/dashboard")
                .then()
                .statusCode(400);

        log.info("Expected status 400 received successfully");
    }

    @Step("Send POST request to create dashboard: {req.name}")
    private int createDashboard(CreateDashboardRequest req) {
        ValidatableResponse response = given()
                .filter(new AllureRestAssured())
                .auth().oauth2(TOKEN)
                .contentType(ContentType.JSON)
                .body(req)
                .when()
                .post("/dashboard")
                .then()
                .statusCode(201);

        return response.extract().jsonPath().getInt("id");
    }

    @Step("Verify dashboard with ID {id} has name '{expectedName}'")
    private void verifyDashboardExistsById(int id, String expectedName) {
        given()
                .filter(new AllureRestAssured())
                .auth().oauth2(TOKEN)
                .when()
                .get("/dashboard/{id}", id)
                .then()
                .statusCode(200)
                .body("name", equalTo(expectedName));
    }
}