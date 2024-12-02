package emre.helper;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

public class DriverImpl {
    private WebDriver driver;

    public WebDriver chromeDriver(String url) {
        ChromeOptions options = new ChromeOptions();

        options.addArguments("--disable-cookies");
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);
        driver.manage().window().fullscreen();
        driver.get(url);

        return driver;
    }

    public WebDriver firefoxDriver() {
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        driver.manage().window().maximize();
        return driver;
    }

    public WebDriver edgeDriver() {
        WebDriverManager.edgedriver().setup();
        driver = new EdgeDriver();
        driver.manage().window().maximize();
        return driver;
    }

    public WebDriver safariDriver() {
        driver = new SafariDriver();
        driver.manage().window().maximize();
        return driver;
    }

}
