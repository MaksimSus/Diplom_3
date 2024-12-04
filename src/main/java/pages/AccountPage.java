package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AccountPage {
    private WebDriver driver;

    // Локаторы
    private final By personalAccountButton = By.xpath("//p[contains(@class, 'AppHeader_header__linkText__3q_va ml-2') and text()='Личный Кабинет']");
    private final By constructorButton = By.xpath("//p[contains(@class, 'AppHeader_header__linkText__3q_va ml-2') and text()='Конструктор']");
    private final By logoButton = By.cssSelector("div.AppHeader_header__logo__2D0X2 > a");
    private final By logoutButton = By.xpath("//button[contains(@class, 'Account_button') and text()='Выход']");
    // Конструктор
    public AccountPage(WebDriver driver) {
        this.driver = driver;
    }

    // Методы для взаимодействия
    public void clickPersonalAccountButton() {
        driver.findElement(personalAccountButton).click();
    }

    public void clickConstructorButton() {
        driver.findElement(constructorButton).click();
    }

    public void clickLogoButton() {
        driver.findElement(logoButton).click();
    }
    public void clickLogoutButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(logoutButton)); // Ждем, пока кнопка станет кликабельной
        driver.findElement(logoutButton).click();
    }
}
