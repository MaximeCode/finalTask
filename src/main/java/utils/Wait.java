package utils;

        import org.openqa.selenium.WebDriver;
        import org.openqa.selenium.support.ui.WebDriverWait;

public class Wait {
    private static WebDriver driver = Driver.getDriver();
    private static WebDriverWait wait;
    private Wait() {}
    public static WebDriverWait getWait() {
        if (wait == null) {
            wait = new WebDriverWait(driver, 3);
        }
        return wait;
    }

    public static void toNull() {
        wait = null;
    }
}
