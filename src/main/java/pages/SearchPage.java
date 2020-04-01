package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Coordinates;
import org.openqa.selenium.interactions.Locatable;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.FieldName;
import utils.Items;
import utils.Wait;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SearchPage extends BasePage {

    @FieldName(name = "максимальная цена")
    @FindBy(xpath = "//div[contains(text(), 'Цена')]/..//label[text()='до']/..//input")
    private WebElement priceInput;

    @FindBy(xpath = "//div[@data-widget='searchResultsSort']//button//span")
    private List<WebElement> searchFilterTexts;

    @FindBy(xpath = "//font[text()='OZON']/../../../..")
    private List<WebElement> itemBlocks;

    @FindBy(xpath = "//div[contains(text(), 'Бренды')]/..")
    private WebElement brandsBlock; //div[contains(text(), 'Бренды')]/..//*[contains(text(), 'Посмотреть все')]

    private By cartLocator = By.xpath(".//div[text()='В корзину']");
    private By priceLocator = By.xpath(".//span[contains(text(), '₽')]");
    private By nameLocator = By.xpath(".//a[contains(@class, 'tile-hover-target')]");


    public void on(String keywords) {
        while (true) {
            try {
                System.out.println("Попытались кликнуть чекбокс <" + keywords + ">.");
                waitVisible(By.xpath("//input/../..//span[contains(text(), '" + keywords + "')]")).click();
                System.out.println("Кликнули по чекбоксу <" + keywords + ">.");
                By locator = By.xpath("//button//span[contains(text(), '" + keywords + "')]");
                waitVisible(locator);
                System.out.println("Дождались надпись фильтра <" + keywords + ">.");
                try {
                    waitInvisible(locator);
                } catch (Exception e) {
                    break;
                }
            } catch (Exception ignored) {}
        }
    }

    public void addBrand(String keywords) {
        try {
            waitClickable(waitVisible((brandsBlock).findElement(
                    By.xpath(".//span[contains(text(), 'Посмотреть все')]")))).click();
            waitStaleness(waitVisible(brandsBlock).findElement(
                    By.xpath(".//span[contains(text(), 'Посмотреть все')]")));
            System.out.println("Кликнули <Посмотрель все>.");
        } catch (Exception ignored) {
        } finally {
            new Actions(driver)
                    .keyDown(waitClickable(brandsBlock.findElement(By.xpath(".//input[@type='']"))), Keys.CONTROL)
                    .sendKeys("a")
                    .keyUp(Keys.CONTROL)
                    .sendKeys(Keys.DELETE + keywords + Keys.ENTER)
                    .perform();
            System.out.println("Ввели <" + keywords + ">.");
            while (true) {
                try {
                    waitClickable(brandsBlock.findElement(
                            By.xpath(".//span[text()='" + keywords + "']"))).click();
                    System.out.println("Кликнули по <" + keywords + ">.");
                    By locator = By.xpath("//button//span[contains(text(), '" + keywords + "')]");
                    waitVisible(locator);
                    System.out.println("Дождались надпись фильтра <" + keywords + ">.");
                    try {
                        waitInvisible(locator);
                    } catch (Exception e) {
                        break;
                    }
                } catch (Exception ignored) {
                }
            }
        }
    }

    public void add(String quantity, String evenOrNot) {
        Items.init();
        int first = 0;
        if (evenOrNot.contains("чёт") && !evenOrNot.contains("нечёт"))
            first = 1;
        while (true) {
            try {
                int number = itemBlocks.size();
                Wait.getWait().until((driver) -> number != itemBlocks.size());
            }
            catch (TimeoutException e) {
                break;
            }
        }
        int number = itemBlocks.size();
        if (quantity.contains("первые") || !quantity.contains("все"))
            number = 2 * Integer.parseInt(quantity.replaceAll("\\D", ""));
        WebElement current;
        for (int i = first; i < number; i += 2) {
            try {
                current = itemBlocks.get(i);
                WebElement element = current.findElement(cartLocator);
                waitClickable(element).click();
                Items.items.put(current.findElements(nameLocator)
                        .stream()
                        .map(WebElement::getText)
                        .filter(e -> e.length() > 15)
                        .findFirst()
                        .orElse(null),
                        current.findElement(priceLocator).getText());
                System.out.println("Добавили товар в корзину и сохранили информацию о нём.");
                waitInvisible(element);
            } catch (NoSuchElementException ignored) {}
        }
    }

    @Override
    WebElement getField(String fieldName) {
        return getField(fieldName, "pages.SearchPage");
    }
}
