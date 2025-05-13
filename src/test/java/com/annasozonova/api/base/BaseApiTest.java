package com.annasozonova.api.base;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for all API tests.
 * Configures common RestAssured setup including base URI, authorization, and request/response logging.
 */
public class BaseApiTest {

    private static final Logger log = LoggerFactory.getLogger(BaseApiTest.class);

    protected static final String BASE_URI = "https://demo.reportportal.io/api/v1";
    protected static final String PROJECT = "default_personal";

    /**
     * API token for authorization.
     */
    protected static final String TOKEN = "personal_tRBigfRnRGiu7k4JICMBXT50JFqKlh2e_uGsXRQQjG6qy5XjkpxrMydMTWLVx0si";

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = BASE_URI + "/" + PROJECT;
        RestAssured.filters(
                new AllureRestAssured(),
                new RequestLoggingFilter(LogDetail.ALL),
                new ResponseLoggingFilter(LogDetail.ALL)
        );

        log.info("RestAssured initialized with base URI: {}", RestAssured.baseURI);
    }
}
