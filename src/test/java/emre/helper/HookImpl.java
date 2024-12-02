package emre.helper;

import org.openqa.selenium.WebDriver;


public class HookImpl extends DriverImpl {
    public static WebDriver driver;

    public WebDriver getDriver(String driverName, String url) {
        if (driverName.equalsIgnoreCase("chrome")) {
            driver = new DriverImpl().chromeDriver(url);
        } else if (driverName.equalsIgnoreCase("firefox")) {
            driver = new DriverImpl().firefoxDriver();
        } else if (driverName.equalsIgnoreCase("edge")) {
            driver = new DriverImpl().edgeDriver();
        } else if (driverName.equalsIgnoreCase("safari")) {
            driver = new DriverImpl().safariDriver();
        } else {
            driver = new DriverImpl().chromeDriver(url);
        }
        return driver;
    }
}