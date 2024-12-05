package tests;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import praktikum.WebDriverCreator;
import pages.ConstructorPage;

import java.time.Duration;

import static org.junit.Assert.assertEquals;

public class ConstructorTabTests {
    private WebDriver driver;

    @Before
    @Step("Set up browser and navigate to the site")
    public void setUp() {
        // Настраиваем браузер
        driver = WebDriverCreator.createWebDriver();
        driver.manage().window().maximize();
        driver.get("https://stellarburgers.nomoreparties.site");
    }

    @After
    @Step("Clean up the browser after tests")
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Switch from 'Buns' to 'Sauces'")
    @Step("Test: Switch from 'Buns' to 'Sauces'")
    public void testSwitchToSauces() {
        ConstructorPage constructorPage = new ConstructorPage(driver);

        // Переход в раздел "Соусы"
        constructorPage.clickSaucesTab();

        // Ожидание и проверка
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(driver -> constructorPage.getActiveTabText().equals("Соусы"));

        assertEquals("Active tab is not 'Sauces'", "Соусы", constructorPage.getActiveTabText());
    }

    @Test
    @DisplayName("Switch from 'Sauces' to 'Fillings'")
    @Step("Test: Switch from 'Sauces' to 'Fillings'")
    public void testSwitchToFillings() {
        ConstructorPage constructorPage = new ConstructorPage(driver);

        // Переход в раздел "Соусы", затем в "Начинки"
        constructorPage.clickSaucesTab();
        constructorPage.clickFillingsTab();

        // Ожидание и проверка
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(driver -> constructorPage.getActiveTabText().equals("Начинки"));

        assertEquals("Active tab is not 'Fillings'", "Начинки", constructorPage.getActiveTabText());
    }

    @Test
    @DisplayName("Switch from 'Fillings' to 'Buns'")
    @Step("Test: Switch from 'Fillings' to 'Buns'")
    public void testSwitchToBuns() {
        ConstructorPage constructorPage = new ConstructorPage(driver);

        // Переход в раздел "Соусы", затем в "Начинки", затем в "Булки"
        constructorPage.clickSaucesTab();
        constructorPage.clickFillingsTab();
        constructorPage.clickBunsTab();

        // Ожидание и проверка
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(7));
        wait.until(driver -> constructorPage.getActiveTabText().equals("Булки"));

        assertEquals("Active tab is not 'Buns'", "Булки", constructorPage.getActiveTabText());
    }
}

