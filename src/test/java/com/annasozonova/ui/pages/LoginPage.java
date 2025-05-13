package com.annasozonova.ui.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Page Object representing the login page of Report Portal.
 */
public class LoginPage {

    private static final Logger log = LoggerFactory.getLogger(LoginPage.class);

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By usernameInputLocator = By.cssSelector("input[placeholder='Login']");
    private final By passwordInputLocator = By.cssSelector("input[placeholder='Password']");
    private final By loginButtonLocator = By.cssSelector("button[type='submit']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Step("Login as user: {username}")
    public void loginAs(String username, String password) {
        log.info("Navigating to the login page");
        driver.get("https://demo.reportportal.io/#login");

        log.debug("Waiting for username input to be present");
        wait.until(ExpectedConditions.presenceOfElementLocated(usernameInputLocator));

        log.info("Filling in username");
        wait.until(ExpectedConditions.elementToBeClickable(usernameInputLocator)).click();
        driver.findElement(usernameInputLocator).sendKeys(Keys.CONTROL + "a", Keys.DELETE);
        driver.findElement(usernameInputLocator).sendKeys(username);

        log.info("Filling in password");
        wait.until(ExpectedConditions.elementToBeClickable(passwordInputLocator)).click();
        driver.findElement(passwordInputLocator).sendKeys(Keys.CONTROL + "a", Keys.DELETE);
        driver.findElement(passwordInputLocator).sendKeys(password);

        log.info("Clicking the Login button");
        wait.until(ExpectedConditions.elementToBeClickable(loginButtonLocator)).click();
    }
}
