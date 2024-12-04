package praktikum;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


public class WebDriverCreator {

    /*
    Переменные окружения, прописанные в системе:
    WEBDRIVERS - путь к папке с драйверами для браузеров
    YANDEX_BROWSER_DRIVER_FILENAME - имя файла драйвера Яндекс браузера (Хромдрайвера нужной версии)
    YANDEX_BROWSER_PATH - путь к исполняемому файлу Яндекс браузера в системе
     */

    public static WebDriver createWebDriver() {
        String browser = System.getProperty("browser");
        if (browser == null) {
            return createChromeDriver();
        }

        switch (browser) {
           case "yandex":// {
//                WebDriverManager.chromedriver().browserVersion("128").setup();
//                ChromeOptions options = new ChromeOptions();
//                options.setBinary("C:/Users/susma/AppData/Local/Yandex/YandexBrowser/Application/browser.exe");
//                return new ChromeDriver(options);
//            }
                return createYandexDriver();
            case "chrome":
            default:
                return createChromeDriver();
        }
    }

    private static WebDriver createChromeDriver() {
        ChromeOptions options = new ChromeOptions();
        return new ChromeDriver(options);
    }


    private static WebDriver createYandexDriver() {
        System.setProperty("webdriver.chrome.driver", "C:/WebDriver/chromedriver-win64/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.setBinary("C:/Users/susma/AppData/Local/Yandex/YandexBrowser/Application/browser.exe");
        return new ChromeDriver(options);

//    private static WebDriver createYandexDriver() {
//        System.setProperty("webdriver.chrome.driver",
//                String.format("%s/%s", System.getenv("C:\\WebDriver\\chromedriver-win64\\chromedriver.exe"),
//                        System.getenv("C:/WebDriver/chromedriver-win64/chromedriver.exe")));
//        ChromeOptions options = new ChromeOptions();
//        options.setBinary(System.getenv("C:/Users/susma/AppData/Local/Yandex/YandexBrowser/Application/browser.exe"));
//        return new ChromeDriver(options);
    }
}