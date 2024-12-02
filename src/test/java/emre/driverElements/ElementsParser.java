package emre.driverElements;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import emre.helper.HookImpl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class ElementsParser extends HookImpl {
    String filePath = "src/test/resources/elements/elements.json";
    List elements = null;
    WebElement element = null;

    public WebElement findElementBy(String elementKey) throws FileNotFoundException {
        FileReader reader = new FileReader(filePath);
        JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();

            String key = jsonObject.get("key").getAsString();
            if (key.equals(elementKey)) {

                String value = jsonObject.get("value").getAsString();
                String type = jsonObject.get("type").getAsString();

                switch (type.toLowerCase()) {
                    case "xpath":
                        return driver.findElement(By.xpath(value));
                    case "id":
                        return driver.findElement(By.id(value));
                    case "classname":
                        return driver.findElement(By.className(value));
                    case "name":
                        return driver.findElement(By.name(value));
                    case "accessibilityid":
                        return driver.findElement(By.xpath("//*[@accessibilityId='" + value + "']"));
                    case "cssselector":
                        return driver.findElement(By.cssSelector(value));
                    default:
                        System.out.println("Unsupported type: " + type);
                        return null;
                }
            }

        }
        return element;
    }

    public List<WebElement> findElementsBy(String elementKey) throws FileNotFoundException, MalformedURLException {
        FileReader reader = new FileReader(filePath);

        JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();

            String key = jsonObject.get("key").getAsString();
            if (key.equals(elementKey)) {

                String value = jsonObject.get("value").getAsString();
                String type = jsonObject.get("type").getAsString();

                switch (type.toLowerCase()) {
                    case "xpath":
                        return driver.findElements(By.xpath(value));
                    case "id":
                        return driver.findElements(By.id(value));
                    case "classname":
                        return driver.findElements(By.className(value));
                    case "name":
                        return driver.findElements(By.name(value));
                    case "accessibilityid":
                        return driver.findElements(By.xpath("//*[@accessibilityId='" + value + "']"));
                    case "cssselector":
                        return driver.findElements(By.cssSelector(value));
                    default:
                        System.out.println("Unsupported type: " + type);
                        return new ArrayList<>();
                }
            }
        }
        return elements;
    }
}