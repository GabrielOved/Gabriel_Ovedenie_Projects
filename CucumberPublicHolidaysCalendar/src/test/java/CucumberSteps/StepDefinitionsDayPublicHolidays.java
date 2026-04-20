package CucumberSteps;

//import of the jollyday custom class and of the Holiday model representing a public holiday
import PublicHolidays.PublicHolidaysJollyDay;
import de.focus_shift.jollyday.core.Holiday;

// Java Cucumber annotations needed to run and create the Gherkin feature files
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

// Java time API for working with dates, months and years
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;

import java.util.*; // Java utility collections (List, Set, Map, etc.)

import static org.junit.Assert.*; // JUnit static assertions for validating test outcomes

/**
 * Service class responsible for retrieving public holidays
 * using the jollyday library for a given day.
 */
public class StepDefinitionsDayPublicHolidays {

    private PublicHolidaysJollyDay holidayService; // Variable needed for the jollyday instantiation
    private List<Holiday> holidays; // List to store the holidays from the public holiday calendar
    private int requestedYear; // Variable for the year that is requested when asking for one or multiple holidays
    private List<Month> requestedMonth = new ArrayList<>(); // List for the months that are requested when asking for one or multiple holidays
    private List<LocalDate> requestedDay = new ArrayList<>(); // List for the day that is requested when asking for one or multiple holidays
    StringBuilder holidayString = new StringBuilder(); // String that is used when requesting for more than one public holiday

    @Before // Instantiation of jollyday and the requestedYear variable to run before the test
    public void jollyDaySetUp() {

        this.holidayService = new PublicHolidaysJollyDay();
        this.requestedYear = Year.now().getValue();

    }


    @Given("the current day")
    public void request_holiday() {
        try {
            // Adding the current date to the empty requested day variable to be used for filtering the next holiday
            this.requestedDay.add(LocalDate.now());
        } catch (IllegalArgumentException | DateTimeException e) { // Exception handling for IllegalArgumentException and DateTimeException
            // Message that gives a little insight to the user about the error and suggests to try again
            System.err.println("Error occurred while requesting holiday: " + e.getMessage() + ". Please try again.\n");
            throw e; // Throwing the exception so that the specific scenario is marked as Failed
        }
    }

    @Given("the current month and the {int} day")
    public void request_holiday_on_day(Integer day) {
        try {
            //  Adding a specific date to the empty requested day variable to be used for filtering the next holiday
            this.requestedDay.add(LocalDate.of(requestedYear, LocalDate.now().getMonth(), day));
        } catch (IllegalArgumentException | DateTimeException e) { // Exception handling for IllegalArgumentException and DateTimeException
            // Message that gives a little insight to the user about the error and suggests to try again
            System.err.println("Error occurred while requesting holiday: " + e.getMessage() + ". Please try again.\n");
            throw e; // Throwing the exception so that the specific scenario is marked as Failed
        }
    }

    @Given("the month of {word} and the {int} day")
    public void request_holiday_on_day_month(String month, Integer day) {
        try {
            // Converting the month string used in @Given to a Month enum
            this.requestedMonth.add(Month.valueOf(month.toUpperCase()));
            // Adding a specific date based on the specified month to the empty requested day variable to be used for filtering the next holiday
            this.requestedDay.add(LocalDate.of(requestedYear, requestedMonth.getFirst(), day));
        } catch (IllegalArgumentException | DateTimeException e) { // Exception handling for IllegalArgumentException and DateTimeException
            // Message that gives a little insight to the user about the error and suggests to try again
            System.err.println("Error occurred while requesting holiday: " + e.getMessage() + ". Please try again.\n");
            throw e; // Throwing the exception so that the specific scenario is marked as Failed
        }
    }

    @Given("the year of {int} and month of {word} and the {int} day")
    public void request_holiday_on_date(Integer year, String month, Integer day) {
        try {
            // Adding the year used in @Given to replace the value in requestedYear
            this.requestedYear = year;
            // Converting the month string used in @Given to a Month enum
            this.requestedMonth.add(Month.valueOf(month.toUpperCase()));
            // Adding a specific date based on the specified month and year to the empty requested day variable to be used for filtering the next holiday
            this.requestedDay.add(LocalDate.of(requestedYear, requestedMonth.getFirst(), day));
        } catch (IllegalArgumentException | DateTimeException e) { // Exception handling for IllegalArgumentException and DateTimeException
            // Message that gives a little insight to the user about the error and suggests to try again
            System.err.println("Error occurred while requesting holiday: " + e.getMessage() + ". Please try again.\n");
            throw e; // Throwing the exception so that the specific scenario is marked as Failed
        }
    }


    @When("asked for the next public holiday")
    public void retrieve_holiday() {

        holidays = holidayService.getHolidays(requestedYear)
                .stream()
                .filter(h -> !h.getDate().isBefore(requestedDay.getFirst()))
                .sorted(Comparator.comparing(Holiday::getDate))
                .limit(1)
                .toList(); // Filtering of the jollyday calendar for the first holiday that is equal or after a specified date

        Optional<Holiday> isDayHoliday = holidayService.getHolidays(requestedYear)
                .stream()
                .filter(h -> h.getDate().isEqual(requestedDay.getFirst()))
                .min(Comparator.comparing(Holiday::getDate)); // Variable that checks if the specified date is a public holiday

        // Check for the holiday list if there are no values and if the date is 26 December
        if (holidays.isEmpty() || (holidays.get(0).getDate().getMonth() == Month.DECEMBER && holidays.get(0).getDate().getDayOfMonth() == 26))
        {
            int nextYear = requestedYear+1; // Variable that takes the next year as 26 December is the last holiday in the Romanian calendar
            holidays = holidayService.getHolidays(nextYear)
                    .stream()
                    .filter(h -> !h.getDate().isBefore(LocalDate.of(nextYear, Month.JANUARY, 1)))
                    .sorted(Comparator.comparing(Holiday::getDate))
                    .limit(2)
                    .toList(); // Filtering of the jollyday calendar for the first two holidays, with the first being equal or after 1 January of next year
            Holiday newYears = holidays.getFirst(); // Variable that takes the first day of New Years
            Holiday afterNewYears = holidays.get(1); // Variable that takes the second day of New Years
            System.out.println("There are no more public holidays in " + requestedYear + "!"); // Print that states there are no more holidays in the requested year
            System.out.println("Next public holidays for " + nextYear + " are " + newYears.getDescription(Locale.ENGLISH) + " - " + newYears.getDate()
                    + " (" + newYears.getDate().getDayOfWeek()
                    + "), " + "and " + afterNewYears.getDescription(Locale.ENGLISH) + " - " + afterNewYears.getDate()
                    + " (" + afterNewYears.getDate().getDayOfWeek()
                    + ")."); // Print for the first and second day of New Years
            System.out.println("<" + "-".repeat(59) + ">"); // Print for separator to provide better result visibility

        }else if (isDayHoliday.isPresent() && (isDayHoliday.get().getDate().getMonth() != Month.DECEMBER ||
                        (isDayHoliday.get().getDate().getDayOfMonth() != 25 &&
                                isDayHoliday.get().getDate().getDayOfMonth() != 26))) // Check that the specified date is a holiday and is different from 25/26 December
        {
            holidays = holidayService.getHolidays(requestedYear)
                    .stream()
                    .filter(h -> !h.getDate().isBefore(requestedDay.getFirst()))
                    .sorted(Comparator.comparing(Holiday::getDate))
                    .limit(2)
                    .toList(); // Filtering of the jollyday calendar for the first two holidays, with the first being equal or after a specified date

            Holiday firstDayHoliday = holidays.getFirst(); // Variable that takes the first holiday
            Holiday secondDayHoliday = holidays.get(1); // Variable that takes the second holiday

            System.out.println("Today is the " + firstDayHoliday.getDate() + " - " +
                    firstDayHoliday.getDescription(Locale.ENGLISH) + " (" + firstDayHoliday.getDate().getDayOfWeek()
                    + ") public holiday, " + "the next holiday is " + secondDayHoliday.getDate() + " - " +
                    secondDayHoliday.getDescription(Locale.ENGLISH) + " (" + secondDayHoliday.getDate().getDayOfWeek()
                    + ")."); // Print for the first and second holidays
            System.out.println("<" + "-".repeat(59) + ">"); // Print for separator to provide better result visibility

        } else if (holidays.get(0).getDate().getMonth() == Month.DECEMBER && holidays.get(0).getDate().getDayOfMonth() == 25) // Check that the specified date is 25 December
        {
            holidays = holidayService.getHolidays(requestedYear)
                    .stream()
                    .filter(h -> !h.getDate().isBefore(requestedDay.getFirst()))
                    .sorted(Comparator.comparing(Holiday::getDate))
                    .limit(2)
                    .toList(); // Filtering of the jollyday calendar for the first two holidays, with the first being equal or after Christmas

            Holiday christmas = holidays.getFirst(); // Variable that takes the first day of Christmas
            Holiday secondDayChristmas = holidays.get(1); // Variable that takes the second day of Christmas

            System.out.println("Today is " + christmas.getDescription(Locale.ENGLISH) + " - " + christmas.getDate()
                    + " (" + christmas.getDate().getDayOfWeek()
                    + "), " + "and tomorrow is " + secondDayChristmas.getDescription(Locale.ENGLISH) + " - " + secondDayChristmas.getDate()
                    + " (" + secondDayChristmas.getDate().getDayOfWeek()
                    + ")."); // Print for the first and second day of Christmas
            System.out.println("<" + "-".repeat(59) + ">"); // Print for separator to provide better result visibility
        } else {
            Holiday holiday = holidays.getFirst(); // Variable that takes the first holiday
            System.out.println("The next public holiday is " + holiday.getDate() + " - " +
                    holiday.getDescription(Locale.ENGLISH) + " (" + holiday.getDate().getDayOfWeek()
                    + ")."); // Print for the first holiday
            System.out.println("<" + "-".repeat(59) + ">"); // Print for separator to provide better result visibility
        }

    }

    @When("asked for the next {int} public holidays")
    public void retrieve_number_of_holidays(Integer holidayNumber) {
        try {
            holidays = holidayService.getHolidays(requestedYear)
                    .stream()
                    .filter(h -> !h.getDate().isBefore(requestedDay.getFirst()))
                    .sorted(Comparator.comparing(Holiday::getDate))
                    .limit(holidayNumber)
                    .toList(); // Filtering of the jollyday calendar for the first holiday that is equal or after a specified date

            Holiday lastHoliday = holidays.getLast(); // Variable that takes the last holiday
            int remainingHolidays = holidays.size(); // Variable that takes number of holidays before any changes
            String holidaysPrint; // String used to print holidays when requesting more than one holiday

            Optional<Holiday> isDayHoliday = holidayService.getHolidays(requestedYear)
                    .stream()
                    .filter(h -> h.getDate().isEqual(requestedDay.getFirst()))
                    .min(Comparator.comparing(Holiday::getDate));  // Variable that checks if the specified date is a public holiday
            // Check for the holiday list if there are no values and if the date is 26 December
            if (holidays.isEmpty() || (holidays.get(0).getDate().getMonth() == Month.DECEMBER && holidays.get(0).getDate().getDayOfMonth() == 26))
            {
                int nextYear = requestedYear + 1; // Variable that takes the next year as 26 December is the last holiday in the Romanian calendar
                holidays = holidayService.getHolidays(nextYear)
                        .stream()
                        .filter(h -> !h.getDate().isBefore(LocalDate.of(nextYear, Month.JANUARY, 1)))
                        .sorted(Comparator.comparing(Holiday::getDate))
                        .limit(2)
                        .toList(); // Filtering of the jollyday calendar for the first two holidays, with the first being equal or after 1 January of next year
                Holiday newYears = holidays.getFirst(); // Variable that takes the first day of New Years
                Holiday afterNewYears = holidays.get(1); // Variable that takes the second day of New Years
                System.out.println("There are no more public holidays in " + requestedYear + "!"); // Print that states there are no more holidays in the requested year
                System.out.println("Next public holidays for " + nextYear + " are " + newYears.getDescription(Locale.ENGLISH) + " - " + newYears.getDate()
                        + " (" + newYears.getDate().getDayOfWeek()
                        + "), " + "and " + afterNewYears.getDescription(Locale.ENGLISH) + " - " + afterNewYears.getDate()
                        + " (" + afterNewYears.getDate().getDayOfWeek()
                        + ")."); // Print for the first and second day of New Years
                System.out.println("<" + "-".repeat(59) + ">"); // Print for separator to provide better result visibility
            } else if (isDayHoliday.isPresent() && (isDayHoliday.get().getDate().getMonth() != Month.DECEMBER ||
                    (isDayHoliday.get().getDate().getDayOfMonth() != 25 &&
                            isDayHoliday.get().getDate().getDayOfMonth() != 26))) // Check that the specified date is a holiday and is different from 25/26 December
            {
                Holiday todayHoliday = holidays.getFirst(); // Variable that takes the first holiday
                holidayString.append("Today is the "); // Append to holidayString
                boolean isFirst = true; // Bool used to check for the first element in for loop

                for (Holiday holiday : holidays) { // for loop that goes through each holiday that was filtered
                    if (isFirst) { // Check for the first element
                        isFirst = false; // Bool is set to false
                        holidayString.append(todayHoliday.getDate() + " - " +
                                todayHoliday.getDescription(Locale.ENGLISH) + " (" + todayHoliday.getDate().getDayOfWeek()
                                + ") public holiday, " + "the next holidays are: "); // Append to holidayString for the first holiday
                    } else if (remainingHolidays < 4 && holiday != lastHoliday) { // Check if the number of holidays is less than 4 and different from the last holiday
                        holidayString.append(holiday.getDate() + " - " +
                                holiday.getDescription(Locale.ENGLISH) + " (" + holiday.getDate().getDayOfWeek()
                                + "), "); // Append to holidayString to show the holiday on one line delimited by comma
                    } else if (remainingHolidays < 4) { // Check if the number of holidays is less than 4
                        holidayString.append(holiday.getDate() + " - " +
                                holiday.getDescription(Locale.ENGLISH) + " (" + holiday.getDate().getDayOfWeek()
                                + ")."); // Append to holidayString to show the holiday on one line for the last holiday
                    } else if (holiday != lastHoliday) { // Check if the holiday is different from the last holiday
                        holidayString.append("\n" + "| " + holiday.getDate() + " - " +
                                holiday.getDescription(Locale.ENGLISH) + " (" + holiday.getDate().getDayOfWeek()
                                + "), "); // Append to holidayString to show the holiday on a different line delimited by comma
                    } else {
                        holidayString.append("\n" + "| " + holiday.getDate() + " - " +
                                holiday.getDescription(Locale.ENGLISH) + " (" + holiday.getDate().getDayOfWeek()
                                + ")."); // Append to holidayString to show the holiday on a different line for the last holiday
                    }
                }
                holidaysPrint = holidayString.toString();  // Converting the StringBuilder to string
                System.out.println(holidaysPrint); // Printing the string in holidaysPrint
                System.out.println("<" + "-".repeat(59) + ">"); // Print for separator to provide better result visibility
            } else if (holidays.get(0).getDate().getMonth() == Month.DECEMBER && holidays.get(0).getDate().getDayOfMonth() == 25) { // Check that the specified date is 25 December
                holidays = holidayService.getHolidays(requestedYear)
                        .stream()
                        .filter(h -> !h.getDate().isBefore(requestedDay.getFirst()))
                        .sorted(Comparator.comparing(Holiday::getDate))
                        .limit(2)
                        .toList(); // Filtering of the jollyday calendar for the first two holidays, with the first being equal or after Christmas

                Holiday christmas = holidays.getFirst(); // Variable that takes the first day of Christmas
                Holiday secondDayChristmas = holidays.get(1); // Variable that takes the second day of Christmas

                System.out.println("Today is " + christmas.getDescription(Locale.ENGLISH) + " - " + christmas.getDate()
                        + " (" + christmas.getDate().getDayOfWeek()
                        + "), " + "and tomorrow is " + secondDayChristmas.getDescription(Locale.ENGLISH) + " - " + secondDayChristmas.getDate()
                        + " (" + secondDayChristmas.getDate().getDayOfWeek()
                        + ")."); // Print for the first and second day of Christmas
                System.out.println("<" + "-".repeat(59) + ">"); // Print for separator to provide better result visibility
            } else {
                holidayString.append("The next public holidays are:"); // Append to holidayString
                for (Holiday holiday : holidays) { // for loop that goes through each holiday that was filtered
                    if (remainingHolidays < 4 && holiday != lastHoliday) { // Check if the number of holidays is less than 4 and different from the last holiday
                        holidayString.append(holiday.getDate() + " - " +
                                holiday.getDescription(Locale.ENGLISH) + " (" + holiday.getDate().getDayOfWeek()
                                + "), "); // Append to holidayString to show the holiday on one line delimited by comma
                    } else if (remainingHolidays < 4) { // Check if the number of holidays is less than 4
                        holidayString.append(holiday.getDate() + " - " +
                                holiday.getDescription(Locale.ENGLISH) + " (" + holiday.getDate().getDayOfWeek()
                                + ")."); // Append to holidayString to show the holiday on one line for the last holiday
                    } else if (holiday != lastHoliday) { // Check if the holiday is different from the last holiday
                        holidayString.append("\n" + "| " + holiday.getDate() + " - " +
                                holiday.getDescription(Locale.ENGLISH) + " (" + holiday.getDate().getDayOfWeek()
                                + "), "); // Append to holidayString to show the holiday on a different line delimited by comma
                    } else {
                        holidayString.append("\n" + "| " + holiday.getDate() + " - " +
                                holiday.getDescription(Locale.ENGLISH) + " (" + holiday.getDate().getDayOfWeek()
                                + ")."); // Append to holidayString to show the holiday on a different line for the last holiday
                    }
                }
                holidaysPrint = holidayString.toString(); // Converting the StringBuilder to string
                System.out.println(holidaysPrint); // Printing the string in holidaysPrint
                System.out.println("<" + "-".repeat(59) + ">"); // Print for separator to provide better result visibility
            }
        } catch (IllegalArgumentException e) { // Exception handling for IllegalArgumentException
            // Message that gives a little insight to the user about the error and suggests to try again
            System.err.println("Error occurred while requesting holiday: " + e.getMessage() + ". Please try again.\n");
            throw e; // Throwing the exception so that the specific scenario is marked as Failed
        }
    }


    @Then("the result should contain the next holiday")
    public void verify_result() {
        // Ensure that the holidays list is populated
        assertNotNull(holidays);
        assertFalse(holidays.isEmpty());

        // Verify the first holiday in the list
        Holiday firstHoliday = holidays.getFirst();
        assertNotNull(firstHoliday);

        // Verify that the holiday is not in the past
        assertFalse(firstHoliday.getDate().isBefore(requestedDay.getFirst()));
    }
}
