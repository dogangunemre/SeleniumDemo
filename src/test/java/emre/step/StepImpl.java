package emre.step;

import emre.driverElements.ElementsParser;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class StepImpl extends ElementsParser {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private WebDriver driver;
    String url = "https://hepsiburada.com/";
    String driverName = "chrome";
    Actions actions;
    JavascriptExecutor js = (JavascriptExecutor) driver;

    Map<Double, String> priceMap = new HashMap<>();
    private double maxPrice = 0;

    @Before
    public void setUp() {
        driver = getDriver(driverName, url);
        actions = new Actions(driver);
    }

    @And("{string} write {string} value to the element")
    public void writeValueToElement(String key, String text) throws FileNotFoundException {
        logger.info("Write {} value to the {} element", text, key);
        findElementBy(key).sendKeys(text);
    }

    @And("Click on the element {string}")
    public void clickElement(String key) throws FileNotFoundException {
        findElementBy(key).click();
        logger.info("{} Click on the element", key);
    }

    @And("Js Click on the element {string}")
    public void clickJsElement(String key) throws FileNotFoundException {
        js.executeScript("arguments[0].click();", findElementBy(key));
        logger.info("{} Js Click on the element", key);
    }

    @And("Hover on the element {string}")
    public void hoverOverElement(String elementKey) throws FileNotFoundException {
        actions.moveToElement(findElementBy(elementKey)).perform(); // Hover over the element
    }


    @And("{string} press the Enter key")
    public void pressEnterKey(String key) throws FileNotFoundException {
        findElementBy(key).sendKeys(Keys.ENTER);
        logger.info("Pressing Enter key on {}", key);
    }

    @Then("{int} seconds wait")
    public void waitForSeconds(int seconds) {
        try {
            logger.info("Wait for {} seconds", seconds);
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Then("Scroll down")
    public void scrollDown() throws InterruptedException {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, 500);");
        Thread.sleep(1000);
    }

    @Then("{string} choose the highest number")
    public void chooseTheHighestNumber(String key) throws InterruptedException, MalformedURLException, FileNotFoundException {
        List<WebElement> priceElements = findElementsBy(key);

        WebElement maxPriceElement = null;

        for (WebElement priceElement : priceElements) {
            String priceText = priceElement.getText().replace(" TL", "")
                    .replace(".", "")
                    .replace(",", ".");

            double price = Double.parseDouble(priceText);

            priceMap.put(price, priceText);

            if (price > maxPrice) {
                maxPrice = price;
                maxPriceElement = priceElement;
            }
        }

        if (maxPriceElement != null) {
            scrollUntilElementFound(maxPriceElement);
        } else {
            System.out.println("Fiyat öğesi bulunamadı.");
        }
    }

    @Then("{string} Verify if price in HashMap matches the displayed price")
    public void verifyPriceInHashMapMatchesTheDisplayedPrice(String key) throws FileNotFoundException {

        String expectedPriceText = priceMap.get(maxPrice);

        WebElement priceElement = findElementBy(key);
        String actualPriceText = priceElement.getText();

        expectedPriceText = expectedPriceText.replace(",", ".");
        actualPriceText = actualPriceText.replace(",", ".");

        double expectedPrice = Double.parseDouble(expectedPriceText.replace(" TL", "").replace(".", ""));
        double actualPrice = Double.parseDouble(actualPriceText.replace(" TL", "").replace(".", ""));

        if (expectedPrice == actualPrice) {
            System.out.println("The price in the HashMap matches the displayed price.");
        } else {
            System.out.println("The price in the HashMap does not match the displayed price.");
        }
    }


    @And("{string} Element Bulunana Kadar Kaydır")
    public void scrollUntilElementFound(WebElement element) {
        boolean elementFound = false;
        int attempts = 0;

        while (!elementFound && attempts < 10) {
            try {
                elementFound = true;
            } catch (Exception e) {
                // Öğeyi bulamazsanız, sayfayı aşağı kaydırmaya devam et
                js.executeScript("window.scrollBy(0, 500);");  // Sayfayı 500px kaydır
                attempts++;
                try {
                    Thread.sleep(1000);  // Kaydırma sonrası 1 saniye bekleyin
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }

        if (elementFound) {
            System.out.println("Element found!");
            element.click();
        } else {
            System.out.println("Element not found after " + attempts + " attempts.");
        }
    }


    @Then("Switch To Window")
    public void switchToWindow() {
        Set<String> windowHandles = driver.getWindowHandles();
        Iterator<String> iterator = windowHandles.iterator();

        String originalWindow = iterator.next();
        String newWindow = iterator.next();

        driver.switchTo().window(newWindow);

        // İşlem sonrası, ilk sekmeye dönmek isterseniz:
        //  driver.switchTo().window(originalWindow);    }
    }

    @After
    public void tearDown() {
        // Test tamamlandıktan sonra tarayıcıyı kapat
        if (driver != null) {
            driver.quit();
        }
    }
}



