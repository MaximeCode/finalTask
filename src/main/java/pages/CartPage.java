package pages;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.Items;

import java.util.ArrayList;
import java.util.List;

public class CartPage extends BasePage {

    @FindBy(xpath = "//span[contains(text(), 'Ваша корзина')]")
    WebElement cartLabel;

    @FindBy(xpath = "//span[contains(text(), 'Удалить выбранные')]")
    WebElement removeLink;

    @FindBy(xpath = "//div[contains(text(), 'Удалить')]")
    WebElement removeAlert;

    @FindBy(xpath = "//h1")
    WebElement header;

    public void checkItems() {
        List<WebElement> items = new ArrayList<>();
        Items.items.keySet().forEach(name -> {
                    try {
                        items.add(waitVisible(By.xpath("//span[text()='" + name + "']")));
                    } catch (Exception ignored) {
                    }
                });
        Assert.assertEquals("Не все добавленные ранее товары находятся в корзине",
                Items.items.size(), items.size());

    }

    public void checkLabel() {
        int number = Integer.parseInt(cartLabel.findElement(
                By.xpath(".//../span[contains(text(), '" + Items.items.size() + " товар')]"))
                .getText()
                .replaceAll("товар.+", "")
                .replaceAll("\\D", ""));
        Assert.assertEquals("Текст <Ваша корзина - " + Items.items.size() + " товаров> не отображается",
                Items.items.size(), number );
    }

    public void removeAll() {
        waitClickable(removeLink).click();
        waitClickable(removeAlert).click();
    }

    public void checkEmpty() {
        Assert.assertTrue("Корзина содержит товары", waitVisible(header).getText().contains("пуста"));
    }

    @Override
    WebElement getField(String fieldName) {
        return null;
    }
}
