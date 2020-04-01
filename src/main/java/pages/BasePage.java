package pages;

import org.openqa.selenium.*;
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

    static WebDriver driver;
    static WebDriverWait wait;

    @FindBy(name = "search")
    WebElement searchInput;

    @FindBy(xpath = "//span[text()='Корзина']")
    WebElement cart;

    BasePage() {
        driver = Driver.getDriver();
        wait = Wait.getWait();
        PageFactory.initElements(driver, this);
    }

    public BasePage search(String keywords) throws InterruptedException {
        waitClickable(searchInput).clear();
        searchInput.sendKeys(keywords + Keys.ENTER);
        return new SearchPage();
    }

    public BasePage toCart() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView()", cart);
        waitClickable(cart).click();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new CartPage();
    }

    public void input(String fieldName, int price) throws ClassNotFoundException {
        WebElement element;
        try {
            element = waitClickable(getField(fieldName));
            element.click();
            element.sendKeys(String.valueOf(price) + Keys.ENTER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    abstract WebElement getField(String fieldName) throws ClassNotFoundException;

    public WebElement getField(String fieldName, String className) throws ClassNotFoundException {
        Class example = Class.forName(className);
        List<Field> fields = Arrays.asList(example.getDeclaredFields());
        Field field = fields
                .stream()
                .filter(element -> element.getAnnotation(FieldName.class).name().equals(fieldName))
                .findFirst()
                .orElse(null);
        if (field != null) {
            System.out.println("Проверка получения xpath:\t" + field.getAnnotation(FindBy.class).xpath());
            return Driver.getDriver().findElement(By.xpath(field.getAnnotation(FindBy.class).xpath()));
        }
        return null;
    }

    WebElement waitVisible(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    WebElement waitClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    List<WebElement> waitVisible(List<WebElement> elements) {
        return wait.until(ExpectedConditions.visibilityOfAllElements(elements));
    }
}
