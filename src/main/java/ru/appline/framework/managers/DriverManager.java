package ru.appline.framework.managers;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

import static ru.appline.framework.utils.PropsConst.*;

public class DriverManager {

    private static DriverManager INSTANCE = null;
    private final TestPropManager properties = TestPropManager.getTestPropManager();
    private WebDriver driver;
    ChromeOptions chromeOptions = new ChromeOptions().addArguments("--disable-notifications");

    private DriverManager() {

    }

    public static DriverManager getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new DriverManager();
        }
        return INSTANCE;
    }

    public WebDriver getDriver() {
        if (driver == null) {
            initDriver();
        }
        return driver;
    }

    public void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    private void initDriver() {
        switch (properties.getProperty(TYPE_BROWSER)) {
            case "firefox":
                System.setProperty("webdriver.gecko.driver", properties.getProperty(PATH_GECKO_DRIVER_WINDOWS));
                driver = new FirefoxDriver();
                break;
            case "chrome":
                System.setProperty("webdriver.chrome.driver", properties.getProperty(PATH_CHROME_DRIVER_WINDOWS));
                driver = new ChromeDriver(chromeOptions);
                break;
            default:
                Assert.fail("Не найден браузер с требуемым названием");
        }
    }
}
