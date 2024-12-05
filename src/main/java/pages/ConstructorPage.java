package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ConstructorPage {
    private WebDriver driver;

    // Локаторы для разделов
    private final By bunsTab = By.xpath("//span[text()='Булки']");
    private final By saucesTab = By.xpath("//span[text()='Соусы']");
    private final By fillingsTab = By.xpath("//span[text()='Начинки']");
    private final By activeTab = By.cssSelector(".tab_tab_type_current__2BEPc"); // Активный таб

    // Конструктор
    public ConstructorPage(WebDriver driver) {
        this.driver = driver;
    }

    // Методы для переключения между разделами
    public void clickBunsTab() {
        driver.findElement(bunsTab).click();
    }

    public void clickSaucesTab() {
        driver.findElement(saucesTab).click();
    }

    public void clickFillingsTab() {
        driver.findElement(fillingsTab).click();
    }

    // Метод для проверки активного таба
    public String getActiveTabText() {
        return driver.findElement(activeTab).getText();
    }
}
