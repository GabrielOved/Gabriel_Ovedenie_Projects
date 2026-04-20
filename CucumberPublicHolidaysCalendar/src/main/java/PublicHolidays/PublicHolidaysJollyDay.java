package PublicHolidays;

// jollyday library classes needed for creating an instance of public holiday calendars
import de.focus_shift.jollyday.core.Holiday;
import de.focus_shift.jollyday.core.HolidayCalendar;
import de.focus_shift.jollyday.core.HolidayManager;
import de.focus_shift.jollyday.core.ManagerParameters;


import java.time.Year; // Java utility for handling year-based date calculations
import java.util.Set; // Collection type used to store and return multiple Holiday objects

/**
 * Service class responsible for retrieving public holidays
 * using the jollyday library for a specific country.
 */
public class PublicHolidaysJollyDay {

    private final HolidayManager holidayManager; // Manages access to holiday data for a specific calendar

    public PublicHolidaysJollyDay() {

        //Initializes the HolidayManager for Romania using jollyday configuration.
        holidayManager = HolidayManager.getInstance(ManagerParameters.create(HolidayCalendar.ROMANIA));

    }

    // Returns all public holidays in a given year.
    public Set<Holiday> getHolidays(int year) {

        return holidayManager.getHolidays(Year.of(year));

    }

}
