package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    private WebDriver driver;

    // Локаторы для страницы логина
    private final By emailFieldLoginPage = By.xpath("//input[@type='text' and @name='name']");
    private final By passwordFieldLoginPage = By.xpath("//input[@type='password' and @name='Пароль']");
    private final By loginButtonLoginPage = By.xpath("//button[contains(@class, 'button_button_type_primary__1O7Bx') and text()='Войти']");

    // Локаторы для кнопок навигации с других страниц
    private final By loginButtonFromMainPage = By.xpath("//button[contains(@class, 'button_button_type_primary__1O7Bx') and text()='Войти в аккаунт']");
    private final By loginButtonFromHeader = By.xpath("//p[contains(@class, 'AppHeader_header__linkText__3q_va ml-2') and text()='Личный Кабинет']");
    private final By loginButtonFromRegistrationForm = By.xpath("//a[text()='Войти']");
    private final By loginButtonFromForgotPasswordForm = By.xpath("//a[text()='Войти']");

    // Конструктор
    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    // Методы для взаимодействия на странице логина
    public void setEmail(String email) {
        driver.findElement(emailFieldLoginPage).sendKeys(email);
    }

    public void setPassword(String password) {
        driver.findElement(passwordFieldLoginPage).sendKeys(password);
    }

    public void clickLoginButton() {
        driver.findElement(loginButtonLoginPage).click();
    }

    public void login(String email, String password) {
        setEmail(email);
        setPassword(password);
        clickLoginButton();
    }

    // Методы для перехода на страницу логина с других страниц
    public void clickLoginButtonFromMainPage() {
        driver.findElement(loginButtonFromMainPage).click();
    }

    public void clickLoginButtonFromHeader() {
        driver.findElement(loginButtonFromHeader).click();
    }

    public void clickLoginButtonFromRegistrationForm() {
        driver.findElement(loginButtonFromRegistrationForm).click();
    }

    public void clickLoginButtonFromForgotPasswordForm() {
        driver.findElement(loginButtonFromForgotPasswordForm).click();
    }
}
