package CucumberSteps;

//import of the jollyday custom class and of the Holiday model representing a public holiday
import PublicHolidays.PublicHolidaysJollyDay;
import de.focus_shift.jollyday.core.Holiday;

// Java Cucumber annotations needed to run and create the Gherkin feature files
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.Year; // Java time API for working years

import java.util.*; // Java utility collections (List, Set, Map, etc.)

import static org.junit.Assert.*; // JUnit static assertions for validating test outcomes

public class StepDefinitionsYearPublicHolidays {

    private PublicHolidaysJollyDay holidayService; // Variable needed for the jollyday instantiation
    private List<Holiday> holidays; // List to store the holidays from the public holiday calendar
    private int requestedYear; // Variable for the year that is requested when asking for multiple public holidays
    private String holidaysPrint; // String used to print holidays when requesting multiple public holidays

    @Before // Instantiation of jollyday to run before the test
    public void jollyDaySetUp() {

        this.holidayService = new PublicHolidaysJollyDay();

    }


    @Given("the current year")
    public void request_all_year_holidays() {

            this.requestedYear = Year.now().getValue(); // Assigning the requestedYear variable the current year

    }

    @Given("the year {int}")
    public void request_specific_year_holidays(Integer year) {

            if (year <= 0) { // Check if the year is negative
                throw new IllegalArgumentException("Year must be positive."); // Thrown IllegalArgumentException so that the specific scenario is marked as Failed
            }
            this.requestedYear = year; // Adding the year used in @Given to replace the value in requestedYear

    }


    @When("asked for all public holidays")
    public void retrieve_all_holidays() {

        holidays = holidayService.getHolidays(requestedYear)
                .stream()
                .sorted(Comparator.comparing(Holiday::getDate))
                .toList(); // Filtering and sorting the jollyday calendar for a given year

        Holiday lastHoliday = holidays.getLast(); // Variable that takes the last holiday
        StringBuilder holidayString = new StringBuilder(); // String that is used when requesting multiple public holidays

        holidayString.append("Below are the public holidays for ").append(requestedYear).append(":\n"); // Print for the given year
        holidays.forEach(holiday -> {if (holiday != lastHoliday) // Check for the last holiday in the for loop
        {
            holidayString.append("| ").append(holiday.getDate().getDayOfWeek()).append(" - ")
                    .append(holiday.getDescription(Locale.ENGLISH)).append(" ").append(holiday.getDate()).append("\n"); // Append to holidayString to show the holiday on a different line
        }else{
            holidayString.append("| ").append(holiday.getDate().getDayOfWeek()).append(" - ")
                    .append(holiday.getDescription(Locale.ENGLISH)).append(" ").append(holiday.getDate());} // Append to holidayString for the last holiday
        });
        holidaysPrint = holidayString.toString(); // Converting the StringBuilder to string

    }


    @Then("the result should contain all public holidays")
    public void verify_all_holidays_result() {
        // Ensure that the holidays list is populated
        assertNotNull(holidays);
        assertFalse(holidays.isEmpty());

        System.out.println(holidaysPrint); // Printing the string in holidaysPrint
        System.out.println("<" + "-".repeat(59) + ">" + "\n"); // Print for separator to provide better result visibility

    }
}

