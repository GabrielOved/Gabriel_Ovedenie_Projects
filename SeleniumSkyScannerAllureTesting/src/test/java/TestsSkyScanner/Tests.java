package TestsSkyScanner;


import SeleniumSkyScanner.Selenium; // Import for Selenium class
import org.openqa.selenium.By; // Selenium locator strategies (find elements by id, xpath, css, etc.)
import org.openqa.selenium.Keys; // Import for Keyboard keys (ENTER, ESCAPE, etc.) for user interactions
import org.openqa.selenium.WebElement; // Import  for web element (button, input, etc.) in the DOM
import org.testng.annotations.BeforeClass; // Import for TestNG annotation - runs once before any test methods in the class
import org.testng.annotations.Test;  // Import for TestNG annotation - marks a method as a test case
import org.testng.annotations.AfterClass; // Import TestNG annotation - runs once after all test methods in the class

import io.qameta.allure.Severity; // Import for Allure annotation - defines severity level of the test (e.g., CRITICAL, BLOCKER)
import io.qameta.allure.SeverityLevel; // Import for Enum used with @Severity to specify severity levels
import io.qameta.allure.Description; // Import for Allure annotation - adds a detailed description to a test
import io.qameta.allure.Epic; // Import for Allure annotation - groups tests under a high-level business requirement
import io.qameta.allure.Feature; // Import for Allure annotation - represents a feature within an epic
import io.qameta.allure.Story; // Import for Allure annotation - describes a user story within a feature
import io.qameta.allure.Step; // Import for Allure annotation - marks a method as a step in test reporting

import java.util.List; // Import for Java utility - represents a collection of elements (used for lists of WebElements)
import java.time.Duration; // Import for Java time utility - used for waits and timeouts (e.g., implicit/explicit waits)
import java.util.NoSuchElementException; // Import for Exception thrown when an element cannot be found in the DOM

// Main test class containing all the Selenium tests
public class Tests extends Selenium {

    @BeforeClass // Runs once before all test methods in this class
    public void seleniumSetUp() { // Initializes the WebDriver instance and opens the SkyScanner homepage
        driver = initialize_driver(); // Start the browser and configure driver
        driver.get("https://www.skyscanner.ro/"); // Navigate to SkyScanner website

    }

    @Severity(SeverityLevel.BLOCKER) // Marks this test as critical (BLOCKER) in Allure reporting
    @Test(priority=1, description="Verify flight Type dropdown") // Defines this as a test case with execution priority and description
    @Description("Verification for the flight Type dropdown.") // Provides a detailed explanation of what the test validates
    @Epic("EP001") // Groups this test under the EP001 Epic
    @Feature("Feature1: flight type") // Specifies the feature being tested
    @Story("Story:Flight Type dropdown") // Describes the specific user story for this test
    @Step("Verify flight Type dropdown") // Logs this action as a step in the test report
    public void flightTypeTest() throws NoSuchElementException
    {
        System.out.println("Running flightTypeTest"); // Print to keep the user informed throughout the process
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // Tells Selenium to wait up to 10 seconds when searching for elements before throwing NoSuchElementException 
        List<WebElement> cookies = driver.findElements(By.xpath(
                "//*[@data-tracking-element-id='cookie_banner_essential_only']")); // List containing the essential cookie element that appears when creating a new browser instance
        if (!cookies.isEmpty()) // Verification if the list is empty to not throw an exception when running all tests simultaneously
        {
            cookies.get(0).click(); // Clicking the cookie element
        }


        driver.findElement(By.xpath(
                "//button[contains(@title, 'Selectează tipul de călătorie')]")).click(); // Locating then clicking the flight type dropdown
        driver.findElement(By.xpath(
                "//span[contains(., 'Doar dus') and contains(@class,'BpkText')]")).click(); // Locating then clicking the first option from the dropdown

        driver.findElement(By.xpath(
                "//button[contains(@title, 'Selectează tipul de călătorie')]")).click(); // Locating then clicking the flight type dropdown
        driver.findElement(By.xpath(
                "//span[contains(., 'Dus-întors') and contains(@class,'BpkText')]")).click(); // Locating then clicking the second option from the dropdown

        driver.findElement(By.xpath(
                "//button[contains(@title, 'Selectează tipul de călătorie')]")).click(); // Locating then clicking the flight type dropdown
        driver.findElement(By.xpath(
                "//span[contains(., 'Destinații multiple') and contains(@class,'BpkText')]")).click(); // Locating then clicking the final option from the dropdown
        
    }

    @Severity(SeverityLevel.BLOCKER) // Marks this test as critical (BLOCKER) in Allure reporting
    @Test(priority=2, description="Verify From and To flight textbox") // Defines this as a test case with execution priority and description
    @Description("Verification for the From and To flight textbox.") // Provides a detailed explanation of what the test validates
    @Epic("EP001") // Groups this test under the EP001 Epic
    @Feature("Feature2: From and To") // Specifies the feature being tested
    @Story("Story:From and To Flight textbox") // Describes the specific user story for this test
    @Step("Verify From and To flight textbox") // Logs this action as a step in the test report
    public void fromToFlightTest() throws NoSuchElementException
    {
        System.out.println("Running fromToFlightTest"); // Print to keep the user informed throughout the process
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // Tells Selenium to wait up to 10 seconds when searching for elements before throwing NoSuchElementException
        List<WebElement> cookies = driver.findElements(By.xpath(
                "//*[@data-tracking-element-id='cookie_banner_essential_only']")); // List containing the essential cookie element that appears when creating a new browser instance
        if (!cookies.isEmpty()) // Verification if the list is empty to not throw an exception when running all tests simultaneously
        {
            cookies.get(0).click(); // Clicking the cookie element
        }


        driver.findElement(By.xpath(
                "//button[contains(@title, 'Selectează tipul de călătorie')]")).click(); // Locating then clicking the Flight type dropdown
        driver.findElement(By.xpath(
                "//span[contains(., 'Doar dus') and contains(@class,'BpkText')]")).click(); // Locating then clicking the first option FROM the dropdown

        driver.findElement(By.id("originInput-input")).sendKeys(Keys.CONTROL + "a"); // Selecting everything in the FROM Flight text box
        driver.findElement(By.id("originInput-input")).sendKeys(Keys.DELETE); // Deleting everything that was selected in the FROM text box to account for the auto generation when first launching the site

        driver.findElement(By.id("originInput-input")).sendKeys("Bucureşti (Oricare)"); // Sending the value in the FROM Flight text box
        driver.findElement(By.id("destinationInput-input")).sendKeys("Roma (Oricare)"); // Sending the value in the TO Flight text box


        driver.findElement(By.id("originInput-input")).sendKeys(Keys.CONTROL + "a"); // Selecting everything in the FROM text box
        driver.findElement(By.id("originInput-input")).sendKeys(Keys.DELETE); // Deleting everything that was selected in the FROM text box to account for carryover when changing flight types
        driver.findElement(By.id("destinationInput-input")).sendKeys(Keys.CONTROL + "a"); // Selecting everything in the TO text box
        driver.findElement(By.id("destinationInput-input")).sendKeys(Keys.DELETE);  // Deleting everything that was selected in the TO text box to account for carryover when changing flight types


        driver.findElement(By.xpath(
                "//button[contains(@title, 'Selectează tipul de călătorie')]")).click(); // Locating then clicking the Flight type dropdown
        driver.findElement(By.xpath(
                "//span[contains(., 'Dus-întors') and contains(@class,'BpkText')]")).click(); // Locating then clicking the second option FROM the dropdown
        driver.findElement(By.id("originInput-input")).sendKeys("Bucureşti (Oricare)"); // Sending the value in the FROM Flight text box
        driver.findElement(By.id("destinationInput-input")).sendKeys("Roma (Oricare)"); // Sending the value in the TO Flight text box


        driver.findElement(By.id("originInput-input")).sendKeys(Keys.CONTROL + "a"); // Selecting everything in the FROM text box
        driver.findElement(By.id("originInput-input")).sendKeys(Keys.DELETE); // Deleting everything that was selected in the FROM text box to account for carryover when changing flight types
        driver.findElement(By.id("destinationInput-input")).sendKeys(Keys.CONTROL + "a");  // Selecting everything in the TO text box
        driver.findElement(By.id("destinationInput-input")).sendKeys(Keys.DELETE); // Deleting everything that was selected in the TO text box to account for carryover when changing flight types


        driver.findElement(By.xpath(
                "//button[contains(@title, 'Selectează tipul de călătorie')]")).click(); // Locating then clicking the Flight type dropdown
        driver.findElement(By.xpath(
                "//span[contains(., 'Destinații multiple') and contains(@class,'BpkText')]")).click(); // Locating then clicking the final option FROM the dropdown
        driver.findElement(By.id("originInput0-input")).sendKeys(Keys.CONTROL + "a"); // Selecting everything in the first multi destination FROM text box
        driver.findElement(By.id("originInput0-input")).sendKeys(Keys.DELETE); // Deleting everything that was selected in the first multi destination FROM text box TO account for carryover when changing flight types
        driver.findElement(By.id("destinationInput1-input")).sendKeys(Keys.CONTROL + "a"); // Selecting everything in the second multi destination TO text box
        driver.findElement(By.id("destinationInput1-input")).sendKeys(Keys.DELETE); // Deleting everything that was selected in the second multi destination TO text box TO account for carryover when changing flight types
        driver.findElement(By.id("originInput0-input")).sendKeys("Bucureşti (Oricare)"); // Sending the value in the first multi destination FROM Flight text box
        driver.findElement(By.id("destinationInput0-input")).sendKeys("Roma (Oricare)"); // Sending the value in the first multi destination TO Flight text box
        driver.findElement(By.id("originInput1-input")).sendKeys("Roma (Oricare)"); // Sending the value in the second multi destination FROM Flight text box
        driver.findElement(By.id("destinationInput1-input")).sendKeys("Bucureşti (Oricare)"); // Sending the value in the second multi destination TO Flight text box

    }

    @Severity(SeverityLevel.BLOCKER) // Marks this test as critical (BLOCKER) in Allure reporting
    @Test(priority=2, description="Verify flight Leaving and Arrival date dropdown")  // Defines this as a test case with execution priority and description
    @Description("Verification for the flight Leaving and Arrival date dropdown.") // Provides a detailed explanation of what the test validates
    @Epic("EP001") // Groups this test under the EP001 Epic
    @Feature("Feature3: Leaving and Arrival date") // Specifies the feature being tested
    @Story("Story:Leaving and Arrival date dropdown") // Describes the specific user story for this test
    @Step("Verify flight Leaving and Arrival date dropdown") // Logs this action as a step in the test report
    public void leavingArrivalDateTest() throws NoSuchElementException
    {
        System.out.println("Running leavingArrivalDateTest"); // Print to keep the user informed throughout the process
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // Tells Selenium to wait up to 10 seconds when searching for elements before throwing NoSuchElementException
        List<WebElement> cookies = driver.findElements(By.xpath(
                "//*[@data-tracking-element-id='cookie_banner_essential_only']")); // List containing the essential cookie element that appears when creating a new browser instance
        if (!cookies.isEmpty()) // Verification if the list is empty to not throw an exception when running all tests simultaneously
        {
            cookies.get(0).click(); // Clicking the cookie element
        }


        driver.findElement(By.xpath(
                "//button[contains(@title, 'Selectează tipul de călătorie')]")).click(); // Locating then clicking the flight type dropdown
        driver.findElement(By.xpath(
                "//span[contains(., 'Dus-întors') and contains(@class,'BpkText')]")).click(); // Locating then clicking the second option from the dropdown

        driver.findElement(By.xpath(
                "//*[contains(@class,'_SearchControlBtn') and contains(@class,'_DesktopBtn')" +
                        "and @data-testid='depart-btn']")).click(); // Locating then clicking the flight Leaving calendar dropdown
        driver.findElement(By.xpath(
                "//button[contains(@class,'bpk-segmented-control--canvas-default-rightOfOption')]")).click(); // Locating then clicking the flexible dates button in the dropdown

        List<WebElement> months = driver.findElements(By.xpath("//*[@data-testid='month-item']")); // List containing all the month items from the calendar dropdown
        months.get(0).click(); // Clicking the first month element
        months = driver.findElements(By.xpath("//*[@data-testid='month-item']")); // Refreshing the list after locating the initial month items as to not raise an NoSuchElementException
        months.get(1).click(); // Clicking the second month element

        driver.findElement(By.xpath(
                "//*[@data-testid='CalendarSearchButton']")).click(); // Locating and clicking the apply calendar dropdown changes


        driver.findElement(By.xpath(
                "//*[contains(@class,'_SearchControlBtn') and contains(@class,'_DesktopBtn')" +
                        "and @data-testid='depart-btn']")).click(); // Locating then clicking the flight Leaving calendar dropdown
        driver.findElement(By.xpath("//button[contains(@class,'BpkSegmentedControl_bpk-segmented-control') " +
                "and contains(@class,'BpkSegmentedControl_bpk-segmented-control--canvas-default') " +
                "and @aria-selected='false']")).click(); // Locating then clicking the specific dates button in the dropdown
        driver.findElement(By.xpath(
                "//*[@data-testid='CalendarSearchButton']")).click(); // Locating and clicking the apply calendar dropdown changes

    }

    @Severity(SeverityLevel.BLOCKER) // Marks this test as critical (BLOCKER) in Allure reporting
    @Test(priority=2, description="Verify Traveler amount and Cabin class dropdown") // Defines this as a test case with execution priority and description
    @Description("Verification for the Traveler amount and Cabin class dropdown.") // Provides a detailed explanation of what the test validates
    @Epic("EP001") // Groups this test under the EP001 Epic
    @Feature("Feature4: Traveler amount and Cabin class") // Specifies the feature being tested
    @Story("Story:Traveler amount and Cabin class dropdown") // Describes the specific user story for this test
    @Step("Verify Traveler amount and Cabin class dropdown") // Logs this action as a step in the test report
    public void travelerAndCabinTest() throws NoSuchElementException
    {
        System.out.println("Running travelerAndCabinTest"); // Print to keep the user informed throughout the process
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // Tells Selenium to wait up to 10 seconds when searching for elements before throwing NoSuchElementException
        List<WebElement> cookies = driver.findElements(By.xpath(
                "//*[@data-tracking-element-id='cookie_banner_essential_only']")); // List containing the essential cookie element that appears when creating a new browser instance
        if (!cookies.isEmpty()) // Verification if the list is empty to not throw an exception when running all tests simultaneously
        {
            cookies.get(0).click(); // Clicking the cookie element
        }

        driver.findElement(By.xpath(
                "//button[contains(@title, 'Selectează tipul de călătorie')]")).click(); // Locating then clicking the flight type dropdown
        driver.findElement(By.xpath(
                "//span[contains(., 'Doar dus') and contains(@class,'BpkText')]")).click(); // Locating then clicking the first option from the dropdown

        driver.findElement(By.xpath(
                "//*[contains(@class,'_SearchControlBtn') " +
                        "and contains(@class,'_Traveller') and @data-testid='traveller-button']")).click(); // Locating then clicking the traveler and cabin amount button
        driver.findElement(By.xpath("//button[contains(@class,'bpk-button') " +
                "and @aria-label='Mărește numărul de adulți']")).click(); // Locating then clicking the add adult number button
        driver.findElement(By.xpath("//button[contains(@class,'bpk-button') " +
                "and @aria-label='Micșorează numărul de adulți']")).click(); // Locating then clicking the subtract adult number button

        driver.findElement(By.xpath("//button[contains(@class,'bpk-button') " +
                "and @aria-label='Mărește numărul de copii']")).click(); // Locating then clicking the add child number button
        driver.findElement(By.xpath("//button[contains(@class,'bpk-button') " +
                "and @aria-label='Micșorează numărul de copii']")).click(); // Locating then clicking the subtract child number button


        driver.findElement(By.xpath(
                "//button[contains(@title, 'Selectează tipul de călătorie')]")).click(); // Locating then clicking the flight type dropdown
        driver.findElement(By.xpath(
                "//span[contains(., 'Dus-întors') and contains(@class,'BpkText')]")).click(); // Locating then clicking the second option from the dropdown
        driver.findElement(By.xpath(
                "//*[contains(@class,'_SearchControlBtn') " +
                        "and contains(@class,'_Traveller') and @data-testid='traveller-button']")).click(); // Locating then clicking the traveler and cabin amount button
        driver.findElement(By.xpath("//button[contains(@class,'bpk-button') " +
                "and @aria-label='Mărește numărul de adulți']")).click(); // Locating then clicking the add adult number button
        driver.findElement(By.xpath("//button[contains(@class,'bpk-button') " +
                "and @aria-label='Micșorează numărul de adulți']")).click(); // Locating then clicking the subtract adult number button

        driver.findElement(By.xpath("//button[contains(@class,'bpk-button') " +
                "and @aria-label='Mărește numărul de copii']")).click(); // Locating then clicking the add child number button
        driver.findElement(By.xpath("//button[contains(@class,'bpk-button')" +
                " and @aria-label='Micșorează numărul de copii']")).click(); // Locating then clicking the subtract child number button


        driver.findElement(By.xpath(
                "//button[contains(@title, 'Selectează tipul de călătorie')]")).click(); // Locating then clicking the flight type dropdown
        driver.findElement(By.xpath(
                "//span[contains(., 'Destinații multiple') and contains(@class,'BpkText')]")).click(); // Locating then clicking the final option from the dropdown
        driver.findElement(By.xpath(
                "//*[contains(@class,'_SearchControlBtn') " +
                        "and contains(@class,'_MultiCityTraveller') and @data-testid='traveller-button']")).click(); // Locating then clicking the traveler and cabin amount button
        driver.findElement(By.xpath("//button[contains(@class,'bpk-button') " +
                "and @aria-label='Mărește numărul de adulți']")).click(); // Locating then clicking the add adult number button
        driver.findElement(By.xpath("//button[contains(@class,'bpk-button') " +
                "and @aria-label='Micșorează numărul de adulți']")).click(); // Locating then clicking the subtract adult number button

        driver.findElement(By.xpath("//button[contains(@class,'bpk-button') " +
                "and @aria-label='Mărește numărul de copii']")).click(); // Locating then clicking the add child number button
        driver.findElement(By.xpath("//button[contains(@class,'bpk-button') " +
                "and @aria-label='Micșorează numărul de copii']")).click(); // Locating then clicking the subtract child number button
    }

    @Severity(SeverityLevel.NORMAL) // Marks this test as critical (NORMAL) in Allure reporting
    @Test(priority=3, description="Verify Nearby Airports checkbox") // Defines this as a test case with execution priority and description
    @Description("Verification for the Nearby Airports checkbox.") // Provides a detailed explanation of what the test validates
    @Epic("EP001") // Groups this test under the EP001 Epic
    @Feature("Feature6: Nearby Airports") // Specifies the feature being tested
    @Story("Story:Nearby Airports checkbox") // Describes the specific user story for this test
    @Step("Verify Nearby Airports checkbox") // Logs this action as a step in the test report
    public void nearbyAirportsTest() throws NoSuchElementException
    {
        System.out.println("Running nearbyAirportsTest"); // Print to keep the user informed throughout the process
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // Tells Selenium to wait up to 10 seconds when searching for elements before throwing NoSuchElementException
        List<WebElement> cookies = driver.findElements(By.xpath(
                "//*[@data-tracking-element-id='cookie_banner_essential_only']")); // List containing the essential cookie element that appears when creating a new browser instance
        if (!cookies.isEmpty()) // Verification if the list is empty to not throw an exception when running all tests simultaneously
        {
            cookies.get(0).click(); // Clicking the cookie element
        }

        driver.findElement(By.xpath(
                "//button[contains(@title, 'Selectează tipul de călătorie')]")).click(); // Locating then clicking the flight type dropdown
        driver.findElement(By.xpath(
                "//span[contains(., 'Doar dus') and contains(@class,'BpkText')]")).click(); // Locating then clicking the first option from the dropdown

        driver.findElement(By.cssSelector(
                "input[name='origin-nearby-airports'][class*='BpkCheckbox_bpk-checkbox__input']")).click(); // Locating then clicking the checkbox for the FROM Flight nearby airport
        driver.findElement(By.cssSelector(
                "input[name='destination-nearby-airports'][class*='BpkCheckbox_bpk-checkbox__input']")).click(); // Locating then clicking the checkbox for the TO Flight nearby airports


        driver.findElement(By.xpath(
                "//button[contains(@title, 'Selectează tipul de călătorie')]")).click(); // Locating then clicking the flight type dropdown
        driver.findElement(By.xpath(
                "//span[contains(., 'Dus-întors') and contains(@class,'BpkText')]")).click(); // Locating then clicking the second option from the dropdown

        driver.findElement(By.cssSelector(
                "input[name='origin-nearby-airports'][class*='BpkCheckbox_bpk-checkbox__input']")).click(); // Locating then clicking the checkbox for the FROM Flight nearby airport
        driver.findElement(By.cssSelector(
                "input[name='destination-nearby-airports'][class*='BpkCheckbox_bpk-checkbox__input']")).click(); // Locating then clicking the checkbox for the TO Flight nearby airports
    }

    @Severity(SeverityLevel.NORMAL) // Marks this test as critical (NORMAL) in Allure reporting
    @Test(priority=3, description="Verify Direct flights checkbox") // Defines this as a test case with execution priority and description
    @Description("Verification for the Direct flights checkbox.") // Provides a detailed explanation of what the test validates
    @Epic("EP001") // Groups this test under the EP001 Epic
    @Feature("Feature7: Direct flights") // Specifies the feature being tested
    @Story("Story:Direct flights checkbox") // Describes the specific user story for this test
    @Step("Verify Direct flights checkbox") // Logs this action as a step in the test report
    public void directFlightsTest() throws NoSuchElementException
    {
        System.out.println("Running directFlightsTest"); // Print to keep the user informed throughout the process
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // Tells Selenium to wait up to 10 seconds when searching for elements before throwing NoSuchElementException
        List<WebElement> cookies = driver.findElements(By.xpath(
                "//*[@data-tracking-element-id='cookie_banner_essential_only']")); // List containing the essential cookie element that appears when creating a new browser instance
        if (!cookies.isEmpty()) // Verification if the list is empty to not throw an exception when running all tests simultaneously
        {
            cookies.get(0).click(); // Clicking the cookie element
        }

        driver.findElement(By.xpath(
                "//button[contains(@title, 'Selectează tipul de călătorie')]")).click(); // Locating then clicking the flight type dropdown
        driver.findElement(By.xpath(
                "//span[contains(., 'Doar dus') and contains(@class,'BpkText')]")).click(); // Locating then clicking the first option from the dropdown

        driver.findElement(By.cssSelector(
                "input[name='prefer-directs'][class*='BpkCheckbox_bpk-checkbox__input']")).click(); // Locating then clicking the checkbox for direct flights
        driver.findElement(By.cssSelector(
                "input[name='prefer-directs'][class*='BpkCheckbox_bpk-checkbox__input']")).click(); // Locating then clicking the checkbox for direct flights


        driver.findElement(By.xpath(
                "//button[contains(@title, 'Selectează tipul de călătorie')]")).click(); // Locating then clicking the flight type dropdown
        driver.findElement(By.xpath(
                "//span[contains(., 'Dus-întors') and contains(@class,'BpkText')]")).click(); // Locating then clicking the second option from the dropdown

        driver.findElement(By.cssSelector(
                "input[name='prefer-directs'][class*='BpkCheckbox_bpk-checkbox__input']")).click(); // Locating then clicking the checkbox for direct flights
        driver.findElement(By.cssSelector(
                "input[name='prefer-directs'][class*='BpkCheckbox_bpk-checkbox__input']")).click(); // Locating then clicking the checkbox for direct flights
    }

    @Severity(SeverityLevel.NORMAL) // Marks this test as critical (NORMAL) in Allure reporting
    @Test(priority=3, description="Verify add Hotel checkbox") // Defines this as a test case with execution priority and description
    @Description("Verification for the add Hotel checkbox.") // Provides a detailed explanation of what the test validates
    @Epic("EP001") // Groups this test under the EP001 Epic
    @Feature("Feature8: Add Hotel") // Specifies the feature being tested
    @Story("Story:Add Hotel checkbox") // Describes the specific user story for this test
    @Step("Verify add Hotel checkbox") // Logs this action as a step in the test report
    public void addHotelTest() throws NoSuchElementException
    {
        System.out.println("Running addHotelTest"); // Print to keep the user informed throughout the process
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // Tells Selenium to wait up to 10 seconds when searching for elements before throwing NoSuchElementException
        List<WebElement> cookies = driver.findElements(By.xpath(
                "//*[@data-tracking-element-id='cookie_banner_essential_only']")); // List containing the essential cookie element that appears when creating a new browser instance
        if (!cookies.isEmpty()) // Verification if the list is empty to not throw an exception when running all tests simultaneously
        {
            cookies.get(0).click(); // Clicking the cookie element
        }
        driver.findElement(By.xpath(
                "//button[contains(@title, 'Selectează tipul de călătorie')]")).click(); // Locating then clicking the flight type dropdown
        driver.findElement(By.xpath(
                "//span[contains(., 'Doar dus') and contains(@class,'BpkText')]")).click(); // Locating then clicking the first option from the dropdown

        driver.findElement(By.cssSelector(
                "input[name='parallel-search-option'][class*='BpkCheckbox_bpk-checkbox__input']")).click(); // Locating then clicking the add hotel to the search checkbox
        driver.findElement(By.cssSelector(
                "input[name='parallel-search-option'][class*='BpkCheckbox_bpk-checkbox__input']")).click(); // Locating then clicking the add hotel to the search checkbox


        driver.findElement(By.xpath(
                "//button[contains(@title, 'Selectează tipul de călătorie')]")).click(); // Locating then clicking the flight type dropdown
        driver.findElement(By.xpath(
                "//span[contains(., 'Dus-întors') and contains(@class,'BpkText')]")).click(); // Locating then clicking the second option from the dropdown

        driver.findElement(By.cssSelector(
                "input[name='parallel-search-option'][class*='BpkCheckbox_bpk-checkbox__input']")).click(); // Locating then clicking the add hotel to the search checkbox
        driver.findElement(By.cssSelector(
                "input[name='parallel-search-option'][class*='BpkCheckbox_bpk-checkbox__input']")).click(); // Locating then clicking the add hotel to the search checkbox
    }

    @Severity(SeverityLevel.BLOCKER) // Marks this test as critical (BLOCKER) in Allure reporting
    // Sets the priority, description and dependencies to other method that need to met before running this method
    @Test(priority=4, description="Verify Search button functionality", dependsOnMethods = {"flightTypeTest", "fromToFlightTest", "leavingArrivalDateTest", "travelerAndCabinTest"})
    @Description("Verification for the Search button after setting the flight type, from and to, leaving and arrival date and traveler amount/cabin class.")
    @Epic("EP001") // Groups this test under the EP001 Epic
    @Feature("Feature5: Search Button") // Specifies the feature being tested
    @Story("Story:Search Button") // Describes the specific user story for this test
    @Step("Verify Search button functionality") // Logs this action as a step in the test report
    public void searchButtonTest() throws NoSuchElementException,InterruptedException
    {
        System.out.println("Running searchButtonTest"); // Print to keep the user informed throughout the process
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // Tells Selenium to wait up to 10 seconds when searching for elements before throwing NoSuchElementException
        List<WebElement> cookies = driver.findElements(By.xpath(
                "//*[@data-tracking-element-id='cookie_banner_essential_only']")); // List containing the essential cookie element that appears when creating a new browser instance
        if (!cookies.isEmpty()) // Verification if the list is empty to not throw an exception when running all tests simultaneously
        {
            cookies.get(0).click(); // Clicking the cookie element
        }


        driver.findElement(By.xpath(
                "//button[contains(@title, 'Selectează tipul de călătorie')]")).click(); // Locating then clicking the flight type dropdown
        driver.findElement(By.xpath(
                "//span[contains(., 'Dus-întors') and contains(@class,'BpkText')]")).click(); // Locating then clicking the second option from the dropdown
        driver.findElement(By.id("originInput-input")).sendKeys(Keys.CONTROL + "a"); // Selecting everything in the FROM text box
        driver.findElement(By.id("originInput-input")).sendKeys(Keys.DELETE); // Deleting everything that was selected in the FROM text box to account for airport carryover
        driver.findElement(By.id("destinationInput-input")).sendKeys(Keys.CONTROL + "a"); // Selecting everything in the TO text box
        driver.findElement(By.id("destinationInput-input")).sendKeys(Keys.DELETE); // Deleting everything that was selected in the TO text box to account for airport carryover
        driver.findElement(By.cssSelector(
                "input[name='parallel-search-option'][class*='BpkCheckbox_bpk-checkbox__input']")).click(); // Locating then clicking the add hotel to the search checkbox to account for carryover when changing flight types
        driver.findElement(By.id("originInput-input")).sendKeys("Bucureşti"); // Sending the value in the FROM Flight text box
        List<WebElement> origin = driver.findElements(
                        By.xpath("//*[contains(@id,'originInput-menu')]//*[@role='option']")); // List containing all the elements to properly select a FROM Flight airport using the written text
        if (!origin.isEmpty()) {
            origin.get(0).click();
        }
        driver.findElement(By.id("destinationInput-input")).sendKeys("Roma"); // Sending the value in the TO Flight text box
        List<WebElement> destination = driver.findElements(
                By.xpath("//*[contains(@id,'destinationInput-menu')]//*[@role='option']")); // List containing all the elements to properly select a TO Flight airport using the written text
        if (!destination.isEmpty()) { // Verification if the list is empty
            destination.get(0).click(); // Clicking the first element
        }


        driver.findElement(By.xpath(
                "//*[contains(@class,'_SearchControlBtn') and contains(@class,'_DesktopBtn')" +
                        "and @data-testid='depart-btn']")).click(); // Locating then clicking the flight Leaving calendar dropdown
        List<WebElement> months = driver.findElements(By.xpath("//*[@data-testid='month-item']")); // List containing all the month items from the calendar dropdown
        months.get(0).click(); // Clicking the first month element
        months = driver.findElements(By.xpath("//*[@data-testid='month-item']")); // Refreshing the list after locating the initial month items as to not raise an NoSuchElementException
        months.get(1).click();// Clicking the second month element

        driver.findElement(By.xpath(
                "//*[@data-testid='CalendarSearchButton']")).click(); // Locating and clicking the apply calendar dropdown changes


        driver.findElement(By.xpath("//input[@name='prefer-directs' " +
                "and contains(@class,'BpkCheckbox_bpk-checkbox__input')]")).click(); // Locating then clicking the checkbox for direct flights
        driver.findElement(By.xpath(
                "//*[contains(@class,'_DesktopCTA')]")).click(); // Locating then clicking the Search Flight button
        Thread.sleep(2000); // Pauses test execution for 2 seconds (2000 milliseconds)

    }

    @AfterClass // Runs once after all test methods in this class have executed
    public void seleniumTearDown() // Method to close the browser and terminates the WebDriver session
    {
        driver.quit(); // Shuts down the browser instance completely
    }

}
