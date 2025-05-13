package com.annasozonova.ui.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Page Object for dashboard-related operations in Report Portal.
 */
public class DashboardPage {

    private static final Logger log = LoggerFactory.getLogger(DashboardPage.class);

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By dashboardsSidebarButton = By.cssSelector("a[href='#default_personal/dashboard']");

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Step("Open dashboard section via sidebar")
    public void openDashboardsPage() {
        log.info("Opening dashboard section from sidebar");
        wait.until(ExpectedConditions.elementToBeClickable(dashboardsSidebarButton)).click();

        // Wait for dashboard list to load
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("a[href*='/dashboard/']")
        ));
    }

    @Step("Open second dashboard in the list")
    public void openDashboard() {
        log.info("Navigating to the first dashboard in the list");
        By secondDashboardLink = By.xpath("(//a[contains(@href, '/dashboard/') and contains(@class, 'dashboardTable__name--')])[1]");
        wait.until(ExpectedConditions.elementToBeClickable(secondDashboardLink)).click();
        wait.until(ExpectedConditions.urlMatches(".*?/dashboard/\\d+$"));
    }

    @Step("Add the first available widget to the dashboard")
    public String addWidget() {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        log.info("Checking and closing modal window if present");
        By modalCloseIcon = By.cssSelector("div[class*='modalHeader__close-modal-icon']");
        if (!driver.findElements(modalCloseIcon).isEmpty()) {
            driver.findElement(modalCloseIcon).click();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(modalCloseIcon));
        }

        log.info("Clicking 'Add new widget' button");
        By addWidgetButton = By.xpath("//span[normalize-space()='Add new widget']/ancestor::button");
        wait.until(ExpectedConditions.elementToBeClickable(addWidgetButton)).click();

        log.debug("Waiting for 'Select widget type' step to become active");
        By widgetStepActiveLabel = By.xpath("//div[contains(@class,'stepLabelItem__step-label-item') and .//div[text()='Select widget type'] and contains(@class,'active')]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(widgetStepActiveLabel));

        log.info("Selecting the first available widget");
        By widgetLabels = By.cssSelector("label[class*='inputRadio__input-radio']");
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(widgetLabels, 0));
        WebElement firstAvailableWidgetLabel = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(widgetLabels)
        ).get(0);

        String widgetName = firstAvailableWidgetLabel.getText().trim();
        log.info("Selected widget: {}", widgetName);
        js.executeScript("arguments[0].click();", firstAvailableWidgetLabel);

        log.info("Proceeding to next step");
        By nextStepBtn = By.xpath("//span[normalize-space()='Next step']/ancestor::button");
        wait.until(ExpectedConditions.elementToBeClickable(nextStepBtn)).click();

        log.info("Selecting the first available filter");
        By filterLabels = By.cssSelector("label[class*='inputRadio__input-radio']");
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(filterLabels, 0));
        WebElement firstAvailableFilter = driver.findElements(filterLabels).stream()
                .filter(WebElement::isDisplayed)
                .filter(label -> label.findElement(By.cssSelector("input[name='filterId']")).isEnabled())
                .findFirst()
                .orElseThrow(() -> {
                    log.error("No available filters found for selection");
                    return new RuntimeException("No available filters to choose from");
                });

        wait.until(ExpectedConditions.elementToBeClickable(firstAvailableFilter)).click();
        wait.until(ExpectedConditions.elementToBeClickable(nextStepBtn)).click();

        log.info("Confirming widget addition");
        By addBtn = By.xpath("//button[normalize-space(text())='Add']");
        wait.until(ExpectedConditions.elementToBeClickable(addBtn)).click();

        log.debug("Verifying widget presence on the dashboard");
        By widgetContainer = By.cssSelector("div.react-grid-item");
        wait.until(ExpectedConditions.presenceOfElementLocated(widgetContainer));

        return widgetName;
    }

    @Step("Verify widget presence by name: {widgetName}")
    public boolean isWidgetPresent(String widgetName) {
        By addedWidget = By.xpath("//div[contains(@class,'widget')]//*[contains(text(),'" + widgetName + "')]");
        return !driver.findElements(addedWidget).isEmpty();
    }
}
