package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class Driver {

    private static WebDriver driver;
    private Driver() {}
    public static WebDriver getDriver() {
        if (driver == null) {
            System.setProperty(TestProperties.getProperty("driver_key"), TestProperties.getProperty("driver_value"));
            if ("firefox".equals(TestProperties.getProperty("browser"))) {
                driver = new FirefoxDriver();
                driver.manage().window().maximize();
            }
            else
                if ("chrome".equals(TestProperties.getProperty("browser")))
                    driver = new ChromeDriver(new ChromeOptions().addArguments("--start-maximized"));
        }
        return driver;
    }
    public static void toNull() {
        driver = null;
    }
}
