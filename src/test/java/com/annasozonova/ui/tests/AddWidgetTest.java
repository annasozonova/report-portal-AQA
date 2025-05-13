package com.annasozonova.ui.tests;

import com.annasozonova.ui.pages.DashboardPage;
import com.annasozonova.ui.pages.LoginPage;
import io.qameta.allure.*;

import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

/**
 * UI test that validates widget creation on a dashboard using Selenium WebDriver.
 * Includes login, navigation, widget addition, and verification of UI state.
 */
@Epic("UI Tests")
@Feature("Widget Management")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AddWidgetTest {

    private WebDriver driver;

    @BeforeEach
    @Step("Initialize browser session")
    void setUp() {
        EdgeOptions options = new EdgeOptions();
        driver = new EdgeDriver(options);
        driver.manage().window().maximize();
    }

    @Test
    @Order(1)
    @Story("Add widget to existing dashboard")
    @DisplayName("Add first available widget to dashboard")
    @Description("Logs in to the application, navigates to the second dashboard, adds the first available widget and verifies its presence")
    void testAddWidget() {
        LoginPage loginPage = new LoginPage(driver);

        Allure.step("Authenticate as default user", () -> {
            loginPage.loginAs("default", "1q2w3e");
        });

        DashboardPage dashboardPage = new DashboardPage(driver);

        Allure.step("Navigate to first dashboard", () -> {
            dashboardPage.openDashboardsPage();
            dashboardPage.openDashboard();
        });

        String widgetName = Allure.step(
                "Add first available widget", dashboardPage::addWidget);

        Allure.step("Verify widget '" + widgetName + "' is present", () -> {
            Assertions.assertTrue(
                    dashboardPage.isWidgetPresent(widgetName),
                    "Widget '" + widgetName + "' should be present"
            );
        });
    }

    @AfterEach
    @Step("Close browser session")
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
