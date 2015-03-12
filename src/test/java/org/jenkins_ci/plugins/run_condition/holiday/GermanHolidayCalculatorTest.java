package org.jenkins_ci.plugins.run_condition.holiday;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;

public class GermanHolidayCalculatorTest {

    @Test
    public void testCalculateEasterSunday() {
        GermanHolidayCalculator calculator = GermanHolidayCalculator.getInstance();

        Calendar easterSunday = calculator.calculateEasterSunday(2012);
        assertEquals(2012, easterSunday.get(Calendar.YEAR));
        assertEquals(Calendar.APRIL, easterSunday.get(Calendar.MONTH));
        assertEquals(8, easterSunday.get(Calendar.DAY_OF_MONTH));

        easterSunday = calculator.calculateEasterSunday(2013);
        assertEquals(2013, easterSunday.get(Calendar.YEAR));
        assertEquals(Calendar.MARCH, easterSunday.get(Calendar.MONTH));
        assertEquals(31, easterSunday.get(Calendar.DAY_OF_MONTH));
    }
}
