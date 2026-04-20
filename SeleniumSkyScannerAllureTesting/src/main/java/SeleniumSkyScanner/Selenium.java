package SeleniumSkyScanner;

import java.time.Duration; // Import for Java time utility - used for waits and timeouts (e.g., implicit/explicit waits)

import org.openqa.selenium.WebDriver; // Selenium interface that controls the browser
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebDriverException;// Chrome-specific WebDriver implementation

import org.openqa.selenium.chrome.ChromeOptions; // Used to customize Chrome browser settings (binary path, arguments, etc.)

// Main class responsible for initializing and managing the Selenium WebDriver instance
public class Selenium {

    public WebDriver driver; // WebDriver instance used to control the browser
    public static ThreadLocal<WebDriver> tdriver = new ThreadLocal<>(); // ThreadLocal to store separate WebDriver instances for parallel execution

    // Method to initialize and configure the WebDriver
    public WebDriver initialize_driver() {
        try {
            //WebDriverManager.chromedriver().setup(); // Alternative approach to Selenium driver: automatically manages the ChromeDriver version by using the installed browser
            System.setProperty("webdriver.chrome.driver", "C:\\Users\\USER_NAME\\PATH_TO_EXE\\chromedriver-win64\\chromedriver.exe"); // Manually setting the path to the ChromeDriver executable
            ChromeOptions options = new ChromeOptions();  // Create ChromeOptions object to customize browser behavior to select the desired chrome.exe
            options.setBinary("C:\\Users\\USER_NAME\\PATH_TO_EXE\\chrome-win64\\chrome.exe");  //Manually setting the path to  the path to the Chrome browser executable
            driver = new ChromeDriver(options);  // Initializes the ChromeDriver with the specified options
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20)); // The maximum time to wait for a page to load
            driver.manage().window().maximize();  // Maximize the browser window
            tdriver.set(driver); // Stores the driver instance in the ThreadLocal variable
            return getDriver();
        } catch (WebDriverException e) {
            // Catch WebDriver-specific exception (e.g., issues with the ChromeDriver binary)
            System.err.println("Error initializing WebDriver: " + e.getMessage());
            throw e; // Rethrow to indicate failure in WebDriver initialization
        } catch (Exception e) {
            // Catch any other exceptions that might occur
            System.err.println("Unexpected error occurred while initializing WebDriver: " + e.getMessage());
            throw e; // Rethrow to indicate failure in WebDriver initialization
        }
    }
    // Thread method to retrieve the WebDriver instance for the current thread
    public static synchronized WebDriver getDriver() {
        return tdriver.get();
    }
}
