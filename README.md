# Report Portal Automated Tests

This project contains UI and API automated tests for the Report Portal demo instance, using Java, Selenium WebDriver, RestAssured, and Allure reporting.

## Project Structure

- `src/test/java/ui` – UI tests using Selenium WebDriver
- `src/test/java/api` – API tests using RestAssured
- `resources` – test data and configuration files

## Test Execution

### Run All Tests

```bash
mvn clean test
```

## Allure Report Generation

To generate and view the test report:

```bash
mvn allure:serve
```

> This will start a local server and open the Allure report in your browser.

## Authentication

The project uses credentials and API keys hardcoded in the `BaseApiTest` class for test purposes.

**Note:** In a real-world project, such credentials should be externalized and secured. For this test assignment, they are embedded directly in the test code to simplify review.

## Project Setup

- Java 17+
- Maven 3.8+
- ChromeDriver or compatible WebDriver binary in PATH
- Internet connection (tests are run against https://demo.reportportal.io)

## Branching Strategy

- `dev` – development branch with latest changes
- `master` – stable branch for final delivery

Pull Request: development is merged into master upon completion of implementation.

## Test Scenarios

### UI Tests
- Login to Report Portal
- Navigate to existing Dashboard
- Add a new widget (first in list)
- Validate widget creation and presence

### API Tests
- Create Dashboard (positive test)
- Attempt creation with invalid input (negative test)
- Validate dashboard presence or rejection

## Design Patterns

- Page Object Model (POM) for UI tests
- AAA (Arrange-Act-Assert) structure for test clarity
- FIRST test principles: Fast, Independent, Repeatable, Self-validating, Timely

## Reporting & Logs

- Allure used for structured, visual test reporting
- Logging implemented for critical actions and assertions
