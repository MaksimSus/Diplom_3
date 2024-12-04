package tests;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.LoginPage;
import praktikum.WebDriverCreator;
import pages.AccountPage;

import java.time.Duration;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.openqa.selenium.support.ui.ExpectedConditions.urlToBe;

public class AccountPageTests {
    private WebDriver driver;
    private String userEmail;
    private String userPassword = "testPassword123";
    private String accessToken;

    private String generateUniqueEmail() {
        return "acc" + System.currentTimeMillis() + "@acc.ru";
    }

    @Before
    @Step("Set up: Register and log in a user, navigate to account page")
    public void setUp() {
        driver = WebDriverCreator.createWebDriver();
        driver.manage().window().maximize();
        userEmail = generateUniqueEmail();
        // Регистрация пользователя через API
        registerUser(userEmail, userPassword);

        // Переход на главную страницу
        driver.get("https://stellarburgers.nomoreparties.site");

        // Выполняем вход через интерфейс
        performLogin(userEmail, userPassword);

        // Переход на страницу личного кабинета
        driver.get("https://stellarburgers.nomoreparties.site/account/profile");
    }

    @Step("Perform login via UI")
    private void performLogin(String email, String password) {
        LoginPage loginPage = new LoginPage(driver);

        // Нажимаем кнопку "Войти в аккаунт" на главной странице
        loginPage.clickLoginButtonFromMainPage();

        // Вводим данные для входа
        loginPage.setEmail(email);
        loginPage.setPassword(password);

        // Подтверждаем вход
        loginPage.clickLoginButton();

        // Ждем, пока страница перезагрузится
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(urlToBe("https://stellarburgers.nomoreparties.site/"));
    }

    @After
    @Step("Clean up: Delete the user and close browser")
    public void tearDown() {
        if (accessToken != null) {
            deleteUser(accessToken);
        }
        if (driver != null) {
            driver.quit();
        }
    }

    @Step("Creating a user via API")
    private void registerUser(String email, String password) {
        Response response = given()
                .header("Content-Type", "application/json")
                .body("{\"email\":\"" + email + "\",\"password\":\"" + password + "\",\"name\":\"Test User\"}")
                .when()
                .post("https://stellarburgers.nomoreparties.site/api/auth/register");

        if (response.getStatusCode() == 200) {
            accessToken = response.jsonPath().getString("accessToken");
        } else {
            Allure.step("Error creating user via API.");
            throw new RuntimeException("Failed to register user: " + response.getBody().asString());
        }
    }

    @Step("Delete a user via API")
    private void deleteUser(String token) {
        Response response = given()
                .header("Authorization", token)
                .when()
                .delete("https://stellarburgers.nomoreparties.site/api/auth/user");

        if (response.getStatusCode() != 202) {
            Allure.step("Error deleting user via API.");
            throw new RuntimeException("Failed to delete user: " + response.getBody().asString());
        }
    }

    @Test
    @DisplayName("Verify navigation to Personal Account page")
    @Step("Test: Click on 'Personal Account'")
    public void testNavigationToPersonalAccount() {
        driver.get("https://stellarburgers.nomoreparties.site");

        AccountPage accountPage = new AccountPage(driver);
        accountPage.clickPersonalAccountButton();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(urlToBe("https://stellarburgers.nomoreparties.site/account/profile"));

        assertEquals("User is not on the Personal Account page.",
                "https://stellarburgers.nomoreparties.site/account/profile", driver.getCurrentUrl());
    }

    @Test
    @DisplayName("Verify navigation to Constructor page from Personal Account")
    @Step("Test: Click on 'Constructor'")
    public void testNavigationToConstructor() {
        driver.get("https://stellarburgers.nomoreparties.site");

        AccountPage accountPage = new AccountPage(driver);
        accountPage.clickConstructorButton();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(urlToBe("https://stellarburgers.nomoreparties.site/"));

        assertEquals("User is not on the Constructor page.",
                "https://stellarburgers.nomoreparties.site/", driver.getCurrentUrl());
    }

    @Test
    @DisplayName("Verify navigation to Home page by clicking on Logo")
    @Step("Test: Click on 'Logo'")
    public void testNavigationToHomeViaLogo() {
        driver.get("https://stellarburgers.nomoreparties.site");

        AccountPage accountPage = new AccountPage(driver);
        accountPage.clickLogoButton();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(urlToBe("https://stellarburgers.nomoreparties.site/"));

        assertEquals("User is not on the Home page after clicking on the logo.",
                "https://stellarburgers.nomoreparties.site/", driver.getCurrentUrl());
    }
}
