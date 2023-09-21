package helpers;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyLoader {

    private static final String PROPERTIES_PATH = "src/test/resources/standproperties/";
    private static final String PROD_PROPERTIES_FILENAME = "prod.properties";
    private static final Properties PROD_PROPERTIES;

    static {
        try {
            PROD_PROPERTIES = new Properties();
            PROD_PROPERTIES.load(new FileInputStream(PROPERTIES_PATH + PROD_PROPERTIES_FILENAME));
        } catch (IOException e) {
            throw new RuntimeException("Не удалось получить настройки из файла " + PROD_PROPERTIES_FILENAME + ":\n", e);
        }
    }

    public static String loadPropertyByName(String name) {
        try {
            return PROD_PROPERTIES.getProperty(name);
        } catch (Exception e) {
            throw new RuntimeException("Не найдена настройка с именем " + name + ":", e);
        }
    }
}
