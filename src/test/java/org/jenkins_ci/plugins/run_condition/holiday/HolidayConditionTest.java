package org.jenkins_ci.plugins.run_condition.holiday;

import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HolidayConditionTest {

    @Test
    public void testIsHolidayNow() {
        HolidayCondition condition = new HolidayCondition(true);

        Calendar now = new GregorianCalendar();
        now.set(Calendar.DAY_OF_MONTH, 1);
        now.set(Calendar.MONTH, Calendar.JANUARY);
        now.set(Calendar.YEAR, 2001);

        assertTrue(condition.isHolidayNow(now, null));

        now.set(Calendar.DAY_OF_MONTH, 2);
        assertFalse(condition.isHolidayNow(now, null));
    }
}
