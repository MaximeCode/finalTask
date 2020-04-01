package utils;

        import org.openqa.selenium.support.ui.WebDriverWait;

public class Wait {
    private static WebDriverWait wait;
    private Wait() {}
    public static WebDriverWait getWait() {
        if (wait == null) {
            wait = new WebDriverWait(Driver.getDriver(), 5);
        }
        return wait;
    }

    public static void toNull() {
        wait = null;
    }
}
