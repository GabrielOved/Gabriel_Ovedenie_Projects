package CucumberSteps;

//import of the jollyday custom class and of the Holiday model representing a public holiday
import PublicHolidays.PublicHolidaysJollyDay;
import de.focus_shift.jollyday.core.Holiday;

// Java Cucumber annotations needed to run and create the Gherkin feature files
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

// Java time API for working with months and years
import java.time.Month;
import java.time.Year;

import java.util.*; // Java utility collections (List, Set, Map, etc.)

/**
 * Service class responsible for retrieving public holidays
 * using the jollyday library for a given month or months.
 */
public class StepDefinitionsMonthPublicHolidays {

    private PublicHolidaysJollyDay holidayService; // Variable needed for the jollyday instantiation
    private List<Holiday> holidays; // List to store the holidays from the public holiday calendar
    private int requestedYear; // Variable for the year that is requested when asking for multiple public holidays
    private List<Month> requestedMonths = new ArrayList<>(); // List for the months that are requested when asking for multiple public holidays
    private StringJoiner monthJoiner = new StringJoiner(", "); // String Joiner used to delimit months by comma
    private String holidaysPrint; // String used to print holidays when requesting multiple public holidays

    @Before // Instantiation of jollyday and the requestedYear variable to run before the test
    public void jollyDaySetUp() {

        this.holidayService = new PublicHolidaysJollyDay();
        this.requestedYear = Year.now().getValue();

    }


    @Given("the current month")
    public void request_holidays_for_month() {

            Month currentMonth = java.time.LocalDate.now().getMonth(); // Assigning the current month to the variable
            this.requestedMonths.add(currentMonth); // Adding the current month to the requestedMonths list

    }

    @Given("the month of {word}")
    public void request_holidays_on_specific_month(String month) {
        try {
            this.requestedMonths.add(Month.valueOf(month.toUpperCase())); // Converting the string to Month then adding it to the requestedMonths list
        } catch (IllegalArgumentException e) { // Exception handling for IllegalArgumentException
            // Message that gives a little insight to the user about the error and suggests to try again
            System.err.println("Error occurred while requesting holidays: " + e.getMessage() + ". Please try again.\n");
            throw e; // Throwing the exception so that the specific scenario is marked as Failed
        }
    }

    @Given("the months of {}")
    public void request_holidays_in_specific_months(String months) {
        try {
            // Array that takes the months delimited by comma in @Given
            String[] monthsArray = months.replaceAll("[()]", "").split("\\s*,\\s*");
            for (String month : monthsArray) { // For loop that goes through all the months in the array
                this.requestedMonths.add(Month.valueOf(month.toUpperCase())); // Converting the string to Month then adding it to the requestedMonths list
            }
        } catch (IllegalArgumentException e) { // Exception handling for IllegalArgumentException
            // Message that gives a little insight to the user about the error and suggests to try again
            System.err.println("Error occurred while requesting holidays: " + e.getMessage() + ". Please try again.\n");
            throw e; // Throwing the exception so that the specific scenario is marked as Failed
        }
    }

    @Given("the months from {word} to {word}")
    public void request_holidays_in_month_range(String startMonth, String endMonth) {
        try {
            for (Month month : Month.values()) // for loop that goes through all the months in a year
            {
                if (month.compareTo(Month.valueOf(startMonth.toUpperCase()))>= 0
                        && month.compareTo(Month.valueOf(endMonth.toUpperCase())) <= 0) // Check for all the months that are between and include the start and end month
                {
                    this.requestedMonths.add(month); // Adding the month to the requestedMonths list
                }
            }
        } catch (IllegalArgumentException e) { // Exception handling for IllegalArgumentException
            // Message that gives a little insight to the user about the error and suggests to try again
            System.err.println("Error occurred while requesting holidays: " + e.getMessage() + ". Please try again.\n");
            throw e; // Throwing the exception so that the specific scenario is marked as Failed
        }
    }


    @When("asked for public holidays")
    public void retrieve_month_holidays() {

        holidays = holidayService.getHolidays(requestedYear)
                .stream()
                .filter(h -> requestedMonths.contains(h.getDate().getMonth()))
                .sorted(Comparator.comparing(Holiday::getDate))
                .toList(); // Filtering of the jollyday calendar for the specified month or months
        requestedMonths.forEach(month -> monthJoiner.add(month.toString())); // Adding all the months to the comma delimiter

        if (holidays.isEmpty()) { // Check for the holiday list if there are no values
            System.out.println("There are no public holidays for " + monthJoiner + "."); // Print that states there are no public holidays in the month
            System.out.println("<" + "-".repeat(59) + ">"); // Print for separator to provide better result visibility
        } else{
            Holiday lastHoliday = holidays.getLast();  // Variable that takes the last holiday
            StringBuilder holidayString = new StringBuilder(); // String that is used when requesting for multiple public holidays

            holidayString.append("Below are the public holidays for ")
                    .append(monthJoiner).append(" ").append(requestedYear).append(":\n"); // Print for the delimited months
            holidays.forEach(holiday -> {if (holiday != lastHoliday) // Check for the last holiday in the for loop
            { holidayString.append("| ").append(holiday.getDate().getDayOfWeek()).append(" - ")
                    .append(holiday.getDescription(Locale.ENGLISH)).append(" ").append(holiday.getDate()).append("\n"); // Append to holidayString to show the holiday on a different line
            } else{
                holidayString.append("| ").append(holiday.getDate().getDayOfWeek()).append(" - ")
                        .append(holiday.getDescription(Locale.ENGLISH)).append(" ").append(holiday.getDate());} // Append to holidayString for the last holiday
            });
            holidaysPrint = holidayString.toString(); // Converting the StringBuilder to string

        }

    }


    @Then("the result should contain the public holidays")
    public void verify_month_holiday_result() {

        if (!holidays.isEmpty()) { // Check for the holiday list if there are values
            System.out.println(holidaysPrint); // Printing the string in holidaysPrint
            System.out.println("<" + "-".repeat(59) + ">"); // Print for separator to provide better result visibility
        }

    }
}