package tests;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import praktikum.WebDriverCreator;
import pages.LoginPage;

import java.time.Duration;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.openqa.selenium.support.ui.ExpectedConditions.urlToBe;

public class LoginTests {
    private WebDriver driver;
    private String userEmail;
    private String userPassword = "strongpassword";
    private String accessToken;

    private String generateUniqueEmail() {
    return "testlog" + System.currentTimeMillis() + "@log.com";
    }

    @Before
    @Step("Set up the environment before tests")
    public void setUp() {
        driver = WebDriverCreator.createWebDriver();
        driver.manage().window().maximize();
        userEmail = generateUniqueEmail();
        // Создаем тестового пользователя через API
        createUserViaApi(userEmail, userPassword);
    }

    @After
    @Step("Clean the environment after tests")
    public void tearDown() {
        if (accessToken != null) {
            deleteUserViaApi(accessToken);
        }
        if (driver != null) {
            driver.quit();
        }
    }

    @Step("Create a user via API")
    private void createUserViaApi(String email, String password) {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/api";
        Response response = given()
                .header("Content-Type", "application/json")
                .body("{\"email\":\"" + email + "\",\"password\":\"" + password + "\",\"name\":\"Test User\"}")
                .when()
                .post("/auth/register");

        if (response.getStatusCode() == 200) {
            accessToken = response.jsonPath().getString("accessToken");
        } else {
            Allure.step("Error creating user via API.");
            throw new RuntimeException("Failed to create user. Response code: " + response.getStatusCode());
        }
    }

    @Step("Delete a user via API")
    private void deleteUserViaApi(String token) {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/api";
            Response deleteResponse = given()
                    .header("Authorization", token)
                    .when()
                    .delete("/auth/user");

            if (deleteResponse.getStatusCode() != 202) {
                Allure.step("Error deleting user via API.");
                throw new RuntimeException("Failed to delete user. Response code: " + deleteResponse.getStatusCode());
            }
        }

    @Test
    @DisplayName("Login through 'Login' button on the main page")
    @Step("Test login via main page")
    public void loginThroughMainPageTest() {
        driver.get("https://stellarburgers.nomoreparties.site");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.clickLoginButtonFromMainPage(); // Переход на страницу логина

        loginPage.login(userEmail, userPassword); // Логин

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(urlToBe("https://stellarburgers.nomoreparties.site/"));

        assertEquals("After logging in, the user was not redirected to the main page.",
                "https://stellarburgers.nomoreparties.site/", driver.getCurrentUrl());
    }

    @Test
    @DisplayName("Login through 'Personal Account' button in the header")
    @Step("Test login via header 'Personal Account'")
    public void loginThroughHeaderTest() {
        driver.get("https://stellarburgers.nomoreparties.site");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.clickLoginButtonFromHeader(); // Переход на страницу логина

        loginPage.login(userEmail, userPassword); // Логин

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(urlToBe("https://stellarburgers.nomoreparties.site/"));

        assertEquals("After logging in through the registration form, the user was not redirected to the main page.",
                "https://stellarburgers.nomoreparties.site/", driver.getCurrentUrl());
    }

    @Test
    @DisplayName("Login through 'Login' button on the registration form")
    @Step("Test login via registration form")
    public void loginThroughRegistrationFormTest() {
        driver.get("https://stellarburgers.nomoreparties.site/register");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.clickLoginButtonFromRegistrationForm(); // Переход на страницу логина

        loginPage.login(userEmail, userPassword); // Логин

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(urlToBe("https://stellarburgers.nomoreparties.site/"));

        assertEquals("After logging in through the registration form, the user was not redirected to the main page.",
                "https://stellarburgers.nomoreparties.site/", driver.getCurrentUrl());
    }

    @Test
    @DisplayName("Login through 'Login' button on the password recovery form")
    @Step("Test login via password recovery form")
    public void loginThroughPasswordRecoveryFormTest() {
        driver.get("https://stellarburgers.nomoreparties.site/forgot-password");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.clickLoginButtonFromForgotPasswordForm(); // Переход на страницу логина

        loginPage.login(userEmail, userPassword); // Логин

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(urlToBe("https://stellarburgers.nomoreparties.site/"));

        assertEquals("After logging in through the registration form, the user was not redirected to the main page.",
                "https://stellarburgers.nomoreparties.site/", driver.getCurrentUrl());
    }
}
