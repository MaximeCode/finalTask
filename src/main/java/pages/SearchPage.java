package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Coordinates;
import org.openqa.selenium.interactions.Locatable;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import utils.FieldName;
import utils.Items;

import java.util.Arrays;
import java.util.List;

public class SearchPage extends BasePage {

    @FieldName(name = "максимальная цена")
    @FindBy(xpath = "//div[contains(text(), 'Цена')]/..//label[text()='до']/..//input")
    private WebElement priceInput;

    @FindBy(xpath = "//aside//label//input/..")
    private List<WebElement> checkboxes;

    @FindBy(xpath = "//div[@data-widget='searchResultsSort']//button//span")
    private List<WebElement> searchFilterTexts;

    @FindBy(xpath = "//font[text()='OZON']/../../../..")
    private List<WebElement> itemBlocks;

    By basketLocator = By.xpath(".//div[text()='В корзину']");
    By priceLocator = By.xpath(".//span[contains(text(), '₽')]");
    By nameLocator = By.xpath(".//a[contains(@class, 'tile-hover-target')]");


    public void on(String keywords) {
        checkboxes
                .stream()
                .filter((element) -> {
                    ((JavascriptExecutor) driver).executeScript(
                            "arguments[0].scrollIntoView()", element);
                    return element
                        .findElement(By.xpath("./..//span"))
                        .getText()
                        .contains(keywords);
                })
                .findFirst()
                .ifPresent(element -> waitClickable(element).click());
    }

    public void add(String quantity, String evenOrNot) {
        Items.init();
        int first = 1;
        if (evenOrNot.contains("нечёт"))
            first = 0;
        int number = waitVisible(itemBlocks).size();
        if (!quantity.contains("все"))
            number = 2 * Integer.parseInt(quantity.replaceAll("\\D", ""));
        WebElement current;
        for (int i = first; i < number; i += 2) {
            try {
                current = itemBlocks.get(i);
                ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].scrollIntoView()", current.findElement(basketLocator));
                Thread.sleep(1000);
                waitClickable(current.findElement(basketLocator)).click();
                Items.items.put(current.findElements(nameLocator)
                        .stream()
                        .map(WebElement::getText)
                        .filter(element -> element.length() > 15)
                        .findFirst()
                        .orElse(null),
                        current.findElement(priceLocator).getText());
            } catch (NoSuchElementException | InterruptedException ignored) {
            }
        }
    }

    @Override
    public WebElement getField(String fieldName) throws ClassNotFoundException {
        return getField(fieldName, "pages.SearchPage");
    }
}
