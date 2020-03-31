package steps;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import io.qameta.allure.Attachment;
import org.openqa.selenium.WebDriver;
import utils.Driver;
import utils.Items;
import utils.TestProperties;
import utils.Wait;

import java.util.Comparator;
import java.util.Map;


public class Hooks {

    static WebDriver driver;

    @Before
    public void gettingStarted() {
        TestProperties.getProperty("url");
        driver = Driver.getDriver();
        driver.get(TestProperties.getProperty("url"));
    }

    @After
    public void shutDown() {
        driver.quit();
        Driver.toNull();
        Wait.toNull();
    }

    @Attachment
    public static byte[] getData() {
        StringBuilder content = new StringBuilder();
        for (Map.Entry<String, String> pair: Items.items.entrySet()) {
            content.append(pair.getKey());
            for (int i = 0; i < 70 - pair.getKey().length(); i++)   //Выравнивание колонок в аттаче
                content.append(" ");
            content.append(pair.getValue()).append("\n");
        }
        int maxPrice = Items
                .items
                .values()
                .stream()
                .map(element -> Integer.parseInt(element.replaceAll("\\D", "")))
                .max(Comparator.comparing(Integer::valueOf))
                .orElse(0);
        Map.Entry<String, String> pairMapPrice = Items
                .items
                .entrySet()
                .stream()
                .filter(element -> Integer.parseInt(element.getValue().replaceAll("\\D", "")) == maxPrice)
                .findFirst()
                .orElse(null);
        content
                .append("\nТовар с наибольшей ценой:\n")
                .append(pairMapPrice.getKey());
        for (int i = 0; i < 70 - pairMapPrice.getKey().length(); i++)   //Выравнивание колонок в аттаче
            content.append(" ");
        content.append(pairMapPrice.getValue()).append("\n");
        return new String(content).getBytes();
    }
}
