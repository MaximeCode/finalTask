package utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class TestProperties {

    private static Properties properties;
    private TestProperties() {}
    public static String getProperty(String key) {
        if (properties == null) {
            properties = new Properties();
            try {
                properties.load(new FileReader("src/main/resources/environment_"
                        + System.getProperty("browser", "chrome") + ".properties"));
            } catch (IOException e) {
                System.out.println("Файл .properties не найден.");
            }
        }
        return properties.getProperty(key);
    }
}
