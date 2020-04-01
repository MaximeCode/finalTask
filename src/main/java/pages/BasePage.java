package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.Driver;
import utils.FieldName;
import utils.Wait;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;


public abstract class BasePage {

    @FindBy(name = "search")
    WebElement searchInput;

    @FindBy(xpath = "//span[text()='Корзина']")
    WebElement cart;

    static WebDriver driver;
    static WebDriverWait wait;


    BasePage() {
        driver = Driver.getDriver();
        PageFactory.initElements(driver, this);
    }

    public BasePage search(String keywords) {
        waitClickable(searchInput).sendKeys(keywords + Keys.ENTER);
        return new SearchPage();
    }

    public void input(String fieldName, int price)  {
        closeAlert();
        System.out.println("Попытались ввести ограничение цены до <" + price + ">.");
        new Actions(driver)
                .keyDown(getField(fieldName), Keys.CONTROL)
                .sendKeys("a")
                .keyUp(Keys.CONTROL)
                .sendKeys(Keys.DELETE + String.valueOf(price) +Keys.ENTER)
                .perform();
        System.out.println("Ввели ограничение цены до <" + price + ">.");
        waitFilterLabel("Цена: от");
    }

    public BasePage toCart() {
        waitClickable(cart).click();
        return new CartPage();
    }

    abstract WebElement getField(String fieldName);

    WebElement getField(String fieldName, String className) {
        Class<?> example = null;
        try {
            example = Class.forName(className);
        } catch (ClassNotFoundException ignored) {}
        assert example != null;
        List<Field> fields = Arrays.asList(example.getDeclaredFields());
        Field field = fields
                .stream()
                .filter(element -> element.getAnnotation(FieldName.class).name().equals(fieldName))
                .findFirst()
                .orElse(null);
        if (field == null)
            return null;
        System.out.println("Получили xpath через Reflection API: " + field.getAnnotation(FindBy.class).xpath() + ".");
        return Driver.getDriver().findElement(By.xpath(field.getAnnotation(FindBy.class).xpath()));
    }

    void waitFilterLabel(String name) {
        By locator = By.xpath("//button//span[contains(text(), '" + name + "')]");
        waitVisible(locator);
        System.out.println("Дождались надпись фильтра <" + name + ">.");
    }

    WebElement waitVisible(WebElement element) {
        return Wait.getWait().until(ExpectedConditions.visibilityOf(element));
    }

    WebElement waitVisible(By locator) {
        return Wait.getWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    void waitInvisible(By locator) {
        Wait.getWait().until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    void waitInvisible(WebElement element) {
        Wait.getWait().until(ExpectedConditions.invisibilityOf(element));
    }

    WebElement waitClickable(WebElement element) {
        return Wait.getWait().until(ExpectedConditions.elementToBeClickable(element));
    }

    WebElement waitClickable(By locator) {
        return Wait.getWait().until(ExpectedConditions.elementToBeClickable(locator));
    }

    WebElement waitPresence(By locator) {
        return Wait.getWait().until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    boolean waitStaleness(WebElement element) {
        return Wait.getWait().until(ExpectedConditions.stalenessOf(element));
    }

    List<WebElement> waitVisible(List<WebElement> elements) {
        return Wait.getWait().until(ExpectedConditions.visibilityOfAllElements(elements));
    }

    private void closeAlert() {
        WebElement element = new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//button[@aria-label='Закрыть сообщение']")));
        System.out.println("Появилось предупреждение.");
        while (true) {
            try {
                System.out.println("Попытались кликнуть, чтобы закрыть предупреждение.");
                waitClickable(element).click();
                System.out.println("Кликнули, чтобы закрыть предупреждение.");
                waitInvisible(element);
                break;
            } catch (Exception ignored) {
            }
        }
    }
}
