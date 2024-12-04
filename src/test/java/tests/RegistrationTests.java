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
import pages.RegistrationPage;

import java.time.Duration;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.openqa.selenium.support.ui.ExpectedConditions.urlToBe;

public class RegistrationTests {
    private WebDriver driver;
    private String userEmail;
    private String userPassword = "strongpassword";
    private boolean userCreated = false;

    @Step("Generate unique email")
    private String generateUniqueEmail() {
        return "testuser" + System.currentTimeMillis() + "@example.com";
    }

    @Before
    @Step("Set up the environment before tests")
    public void setUp() {
        driver = WebDriverCreator.createWebDriver();
        driver.manage().window().maximize();
        userEmail = generateUniqueEmail();
    }

    @After
    @Step("Clean the environment after tests")
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }

        // Удаление пользователя только если он был создан
        if (userCreated) {
            deleteUserViaApi(userEmail, userPassword);
        }
    }

    @Step("Delete the user, use API")
    private void deleteUserViaApi(String email, String password) {
        // Удаление пользователя через API
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/api";

        // Получение токена через логин
        Response loginResponse = given()
                .header("Content-Type", "application/json")
                .body(String.format("{\"email\":\"%s\",\"password\":\"%s\"}", email, password))
                .when()
                .post("/auth/login");

        if (loginResponse.getStatusCode() == 200) {
            String accessToken = loginResponse.jsonPath().getString("accessToken");

            // Удаление пользователя через API
            given()
                    .header("Authorization", accessToken)
                    .when()
                    .delete("/auth/user")
                    .then()
                    .statusCode(202);
        } else {
            Allure.step("Не удалось получить токен для удаления пользователя. Пользователь мог не создаться.");
        }
    }

    @Test
    @DisplayName("Successful user registration")
    @Step("Register user")
    public void successfulRegistrationTest() {
        // Регистрируем пользователя
        driver.get("https://stellarburgers.nomoreparties.site/register");

        RegistrationPage registrationPage = new RegistrationPage(driver);
        registrationPage.setName("Test User");
        registrationPage.setEmail(userEmail);
        registrationPage.setPassword(userPassword);
        registrationPage.clickRegister();

        userCreated = true; // Устанавливаем флаг, что пользователь создан

        // Ожидание редиректа
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(urlToBe("https://stellarburgers.nomoreparties.site/login"));

        // Проверка редиректа
        String expectedUrl = "https://stellarburgers.nomoreparties.site/login";
        assertEquals("Пользователь не был перенаправлен на страницу логина после регистрации",
                expectedUrl, driver.getCurrentUrl());
    }

    @Test
    @DisplayName("Incorrect password registration")
    @Step("Register user with incorrect password")
    public void invalidPasswordTest() {
        // Проверяем регистрацию с некорректным паролем
        driver.get("https://stellarburgers.nomoreparties.site/register");

        RegistrationPage registrationPage = new RegistrationPage(driver);
        registrationPage.setName("Test User");
        registrationPage.setEmail(userEmail);
        registrationPage.setPassword("123"); // Некорректный пароль
        registrationPage.clickRegister();

        // Ожидание появления сообщения об ошибке
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        String actualError = wait.until(driver -> registrationPage.getErrorMessage());

        // Ожидаемое сообщение
        String expectedError = "Некорректный пароль";
        assertEquals("Сообщение об ошибке не совпадает с ожидаемым", expectedError, actualError);
    }
}
