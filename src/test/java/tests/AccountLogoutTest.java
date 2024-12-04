package tests;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import praktikum.WebDriverCreator;
import pages.AccountPage;
import pages.LoginPage;

import java.time.Duration;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.openqa.selenium.support.ui.ExpectedConditions.urlToBe;

public class AccountLogoutTest {
    private WebDriver driver;
    private String userEmail = "logout_user@kz.kz";
    private String userPassword = "testPassword123";
    private String accessToken;


    @Before
    @Step("Set up: Register user, log in via UI, and navigate to personal account")
    public void setUp() {
        driver = WebDriverCreator.createWebDriver();
        driver.manage().window().maximize();

        // Регистрация пользователя через API
        registerUser(userEmail, userPassword);

        // Логин через UI
        driver.get("https://stellarburgers.nomoreparties.site");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.clickLoginButtonFromMainPage();
        loginPage.setEmail(userEmail);
        loginPage.setPassword(userPassword);
        loginPage.clickLoginButton();

        // Переход в личный кабинет
        AccountPage accountPage = new AccountPage(driver);
        accountPage.clickPersonalAccountButton();
    }

    @After
    @Step("Clean up: Close the browser")
    public void tearDown() {
        if (accessToken != null) {
            deleteUser(accessToken);
        }
        if (driver != null) {
            driver.quit();
        }
    }

    @Step("Register a user via API")
    private void registerUser(String email, String password) {
        Response response = given()
                .header("Content-Type", "application/json")
                .body("{\"email\":\"" + email + "\",\"password\":\"" + password + "\",\"name\":\"Test User\"}")
                .when()
                .post("https://stellarburgers.nomoreparties.site/api/auth/register");

        if (response.getStatusCode() == 200) {
            accessToken = response.jsonPath().getString("accessToken");
        } else {
            throw new RuntimeException("Failed to register user: " + response.getBody().asString());
        }
    }

    @Step("Delete user with token")
    private void deleteUser(String accessToken) {
        // Удаляем пользователя
        Response response = given()
                .header("Authorization", accessToken)
                .when()
                .delete("https://stellarburgers.nomoreparties.site/api/auth/user");

        if (response.getStatusCode() != 202) {
            throw new RuntimeException("Failed to delete user: " + response.getBody().asString());
        }
    }

    @Test
    @DisplayName("Verify logout from personal account")
    @Step("Test: Click on 'Logout' and verify redirection")
    public void testLogout() {
        AccountPage accountPage = new AccountPage(driver);
        // Нажать кнопку выхода
        accountPage.clickLogoutButton();

        // Ожидание перехода на страницу входа
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(urlToBe("https://stellarburgers.nomoreparties.site/login"));

        // Проверить, что пользователь был перенаправлен на страницу входа
        assertEquals("User is not on the Login page.",
                "https://stellarburgers.nomoreparties.site/login", driver.getCurrentUrl());
    }
}
