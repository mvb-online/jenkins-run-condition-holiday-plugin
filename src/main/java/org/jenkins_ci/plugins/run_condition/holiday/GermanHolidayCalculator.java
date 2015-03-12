package org.jenkins_ci.plugins.run_condition.holiday;

import hudson.Extension;
import hudson.init.InitMilestone;
import hudson.init.Initializer;
import hudson.util.CopyOnWriteMap;

import java.util.*;

@Extension
public class GermanHolidayCalculator {

    private static Map<Integer, List<Calendar>> publicHolidays = new HashMap<Integer, List<Calendar>>();

    private static GermanHolidayCalculator germanHolidayCalculator = null;

    public GermanHolidayCalculator() {
        super();
    }

    @Initializer(after=InitMilestone.PLUGINS_STARTED)
    public static void init() {
        if (germanHolidayCalculator == null) {
            germanHolidayCalculator = new GermanHolidayCalculator();
        }

        Calendar now = Calendar.getInstance();

        int year = now.get(Calendar.YEAR);

        for (int i = 0; i < 6; i++) {
            year = year + i;
            List<Calendar> holidays = germanHolidayCalculator.calculatePublicHolidays(year);
            publicHolidays.put(year, holidays);
        }
    }

    public static GermanHolidayCalculator getInstance() {
        if (germanHolidayCalculator == null) {
            germanHolidayCalculator = new GermanHolidayCalculator();
        }

        return germanHolidayCalculator;
    }

    public boolean isHoliday(Calendar date) {
        int year = date.get(Calendar.YEAR);

        if (publicHolidays.isEmpty() || !publicHolidays.containsKey(year)) {
            List<Calendar> holidays = calculatePublicHolidays(year);
            publicHolidays.put(year, holidays);
        }

        return isHoliday(date, publicHolidays.get(year));
    }

    public boolean isHoliday(Calendar date, List<Calendar> holidays) {
        for (Calendar cal : holidays) {
            if (cal.get(Calendar.YEAR) == date.get(Calendar.YEAR) &&
                cal.get(Calendar.MONTH) == date.get(Calendar.MONTH) &&
                cal.get(Calendar.DAY_OF_MONTH) == date.get(Calendar.DAY_OF_MONTH)) {

                return true;
            }
        }

        return false;
    }

    /**
     * Calculates the Eastern Sunday, this is the base for several other public holidays in germany.
     *
     * @param year
     * @return
     */
    public Calendar calculateEasterSunday(int year) {
        if (year <= 1582) {
            throw new IllegalArgumentException(
                "Algorithm invalid before April 1583");
        }
        int golden, century, x, z, d, epact, n;

        golden = (year % 19) + 1; /* E1: metonic cycle */
        century = (year / 100) + 1; /* E2: e.g. 1984 was in 20th C */
        x = (3 * century / 4) - 12; /* E3: leap year correction */
        z = ((8 * century + 5) / 25) - 5; /* E3: sync with moon's orbit */
        d = (5 * year / 4) - x - 10;
        epact = (11 * golden + 20 + z - x) % 30; /* E5: epact */
        if ((epact == 25 && golden > 11) || epact == 24)
            epact++;
        n = 44 - epact;
        n += 30 * (n < 21 ? 1 : 0); /* E6: */
        n += 7 - ((d + n) % 7);
        if (n > 31) /* E7: */
            return new GregorianCalendar(year, 4 - 1, n - 31); /* April */
        else
            return new GregorianCalendar(year, 3 - 1, n); /* March */
    }

    /**
     * Returns a list of all necessary public holidays in germany.
     *
     * @param year
     * @return
     */
    public List<Calendar> calculatePublicHolidays(int year) {
        List<Calendar> holidays = new ArrayList<Calendar>();

        Calendar gc_ostersonntag = this.calculateEasterSunday(year);
        holidays.add(gc_ostersonntag);

        Calendar gc_neujahr = new GregorianCalendar(gc_ostersonntag.get(Calendar.YEAR), 0, 1);
        holidays.add(gc_neujahr);

        Calendar gc_ostermontag = new GregorianCalendar(gc_ostersonntag.get(Calendar.YEAR), gc_ostersonntag.get(Calendar.MONTH), (gc_ostersonntag.get(Calendar.DATE) + 1));
        holidays.add(gc_ostermontag);

        Calendar gc_karfreitag = new GregorianCalendar(gc_ostersonntag.get(Calendar.YEAR), gc_ostersonntag.get(Calendar.MONTH), (gc_ostersonntag.get(Calendar.DATE) - 2));
        holidays.add(gc_karfreitag);

        Calendar gc_christihimmelfahrt = new GregorianCalendar(gc_ostersonntag.get(Calendar.YEAR), gc_ostersonntag.get(Calendar.MONTH), (gc_ostersonntag.get(Calendar.DATE) + 39));
        holidays.add(gc_christihimmelfahrt);

        Calendar gc_pfingstsonntag = new GregorianCalendar(gc_ostersonntag.get(Calendar.YEAR), gc_ostersonntag.get(Calendar.MONTH), (gc_ostersonntag.get(Calendar.DATE) + 49));
        holidays.add(gc_pfingstsonntag);

        Calendar gc_pfingstmontag = new GregorianCalendar(gc_ostersonntag.get(Calendar.YEAR), gc_ostersonntag.get(Calendar.MONTH), (gc_ostersonntag.get(Calendar.DATE) + 50));
        holidays.add(gc_pfingstmontag);

// not an official german holiday...
//        Calendar gc_frohnleichnahm = new GregorianCalendar(gc_ostersonntag.get(Calendar.YEAR), gc_ostersonntag.get(Calendar.MONTH), (gc_ostersonntag.get(Calendar.DATE) + 60));
//        holidays.add(gc_frohnleichnahm);

        Calendar gc_wiedervereinigung = new GregorianCalendar(gc_ostersonntag.get(Calendar.YEAR), 9, 1);
        holidays.add(gc_wiedervereinigung);

        Calendar gc_weihnachten_2 = new GregorianCalendar(gc_ostersonntag.get(Calendar.YEAR), 11, 25);
        holidays.add(gc_weihnachten_2);

        Calendar gc_weihnachten_3 = new GregorianCalendar(gc_ostersonntag.get(Calendar.YEAR), 11, 26);
        holidays.add(gc_weihnachten_3);

        Calendar gc_silvester = new GregorianCalendar(gc_ostersonntag.get(Calendar.YEAR), 11, 31);
        holidays.add(gc_silvester);

        return holidays;
    }

}
