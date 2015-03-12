package org.jenkins_ci.plugins.run_condition.holiday;

import hudson.Extension;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import org.jenkins_ci.plugins.run_condition.RunCondition;
import org.kohsuke.stapler.DataBoundConstructor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HolidayCondition extends RunCondition {

    final boolean onlyOnRealWorkdays;

    @DataBoundConstructor
    public HolidayCondition(final boolean onlyOnRealWorkdays) {
        this.onlyOnRealWorkdays = onlyOnRealWorkdays;
    }

    public boolean isOnlyOnRealWorkdays() {
        return onlyOnRealWorkdays;
    }

    @Override
    public boolean runPrebuild(final AbstractBuild<?, ?> build, final BuildListener listener) {
        return evaluate(build, listener);
    }

    @Override
    public boolean runPerform(final AbstractBuild<?, ?> build, final BuildListener listener) {
        return evaluate(build, listener);
    }

    protected boolean evaluate(final AbstractBuild<?, ?> build, final BuildListener listener) {
        final Calendar now = Calendar.getInstance();
        if (onlyOnRealWorkdays && isHolidayNow(now, listener)) return false;
        return true;        
    }

    /**
     * Check if the given date is a holiday.
     *
     * @param date
     * @param listener
     * @return
     */
    protected boolean isHolidayNow(final Calendar date, final BuildListener listener) {
        GermanHolidayCalculator calculator = GermanHolidayCalculator.getInstance();
        Boolean isHoliday = calculator.isHoliday(date);

        final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String holidayStr = isHoliday ? "" : "not (yet)";

        if (listener != null) {
            listener.getLogger().println(Messages.onlyOnRealWorkdays_console(dateFormat.format(date.getTime()), holidayStr));
        }

        return calculator.isHoliday(date);
    }

    @Extension
    public static class HolidayConditionDescriptor extends RunConditionDescriptor {

        @Override
        public String getDisplayName() {
            return Messages.holidayCondition_displayName();
        }

    }

}
