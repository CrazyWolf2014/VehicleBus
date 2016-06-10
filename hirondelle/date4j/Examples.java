package hirondelle.date4j;

import hirondelle.date4j.DateTime.DayOverflow;
import java.util.Locale;
import java.util.TimeZone;

public final class Examples {
    private void ageIfBornOnCertainDate() {
        DateTime today = DateTime.today(TimeZone.getDefault());
        DateTime forDateOnly = DateTime.forDateOnly(Integer.valueOf(1995), Integer.valueOf(5), Integer.valueOf(16));
        int intValue = today.getYear().intValue() - forDateOnly.getYear().intValue();
        if (today.getDayOfYear().intValue() < forDateOnly.getDayOfYear().intValue()) {
            intValue--;
        }
        log("Age of someone born May 16, 1995 is : " + intValue);
    }

    private void currentDateTime() {
        log("Current date-time in default time zone : " + DateTime.now(TimeZone.getDefault()).format("YYYY-MM-DD hh:mm:ss"));
    }

    private void currentDateTimeInCairo() {
        log("Current date-time in Cairo : " + DateTime.now(TimeZone.getTimeZone("Africa/Cairo")).format("YYYY-MM-DD hh:mm:ss (WWWW)", Locale.getDefault()));
    }

    private void daysTillChristmas() {
        DateTime today = DateTime.today(TimeZone.getDefault());
        DateTime forDateOnly = DateTime.forDateOnly(today.getYear(), Integer.valueOf(12), Integer.valueOf(25));
        int i = 0;
        if (!today.isSameDayAs(forDateOnly)) {
            if (today.lt(forDateOnly)) {
                i = today.numDaysFrom(forDateOnly);
            } else if (today.gt(forDateOnly)) {
                i = today.numDaysFrom(DateTime.forDateOnly(Integer.valueOf(today.getYear().intValue() + 1), Integer.valueOf(12), Integer.valueOf(25)));
            }
        }
        log("Number of days till Christmas : " + i);
    }

    private void firstDayOfThisWeek() {
        Object today = DateTime.today(TimeZone.getDefault());
        int intValue = today.getWeekDay().intValue();
        if (intValue > 1) {
            today = today.minusDays(Integer.valueOf(intValue - 1));
        }
        log("The first day of this week is : " + today);
    }

    private void hoursDifferenceBetweenParisAndPerth() {
        int intValue = DateTime.now(TimeZone.getTimeZone("Australia/Perth")).getHour().intValue() - DateTime.now(TimeZone.getTimeZone("Europe/Paris")).getHour().intValue();
        if (intValue < 0) {
            intValue += 24;
        }
        log("Numbers of hours difference between Paris and Perth : " + intValue);
    }

    private void imitateISOFormat() {
        log("Output using an ISO format: " + DateTime.now(TimeZone.getDefault()).format("YYYY-MM-DDThh:mm:ss"));
    }

    private void jdkDatesSuctorial() {
        log("The number of years the JDK date-time API has been suctorial : " + (DateTime.today(TimeZone.getDefault()).getYear().intValue() - DateTime.forDateOnly(Integer.valueOf(1996), Integer.valueOf(1), Integer.valueOf(23)).getYear().intValue()));
    }

    private static void log(Object obj) {
        System.out.println(String.valueOf(obj));
    }

    public static void main(String... strArr) {
        Examples examples = new Examples();
        examples.currentDateTime();
        examples.currentDateTimeInCairo();
        examples.ageIfBornOnCertainDate();
        examples.optionsExpiry();
        examples.daysTillChristmas();
        examples.whenIs90DaysFromToday();
        examples.whenIs3Months5DaysFromToday();
        examples.hoursDifferenceBetweenParisAndPerth();
        examples.weeksSinceStart();
        examples.timeTillMidnight();
        examples.imitateISOFormat();
        examples.firstDayOfThisWeek();
        examples.jdkDatesSuctorial();
    }

    private void optionsExpiry() {
        DateTime startOfMonth = DateTime.today(TimeZone.getDefault()).getStartOfMonth();
        log("The 3rd Friday of this month is : " + DateTime.forDateOnly(startOfMonth.getYear(), startOfMonth.getMonth(), Integer.valueOf(startOfMonth.getWeekDay().intValue() == 7 ? 21 : 21 - startOfMonth.getWeekDay().intValue())).format("YYYY-MM-DD"));
    }

    private void timeTillMidnight() {
        DateTime now = DateTime.now(TimeZone.getDefault());
        log("This many seconds till midnight : " + now.numSecondsFrom(now.plusDays(Integer.valueOf(1)).getStartOfDay()));
    }

    private void weeksSinceStart() {
        log("The number of weeks since Sep 6, 2010 : " + (DateTime.today(TimeZone.getDefault()).getWeekIndex().intValue() - DateTime.forDateOnly(Integer.valueOf(2010), Integer.valueOf(9), Integer.valueOf(6)).getWeekIndex().intValue()));
    }

    private void whenIs3Months5DaysFromToday() {
        log("3 months and 5 days from today is : " + DateTime.today(TimeZone.getDefault()).plus(Integer.valueOf(0), Integer.valueOf(3), Integer.valueOf(5), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), DayOverflow.FirstDay).format("YYYY-MM-DD"));
    }

    private void whenIs90DaysFromToday() {
        log("90 days from today is : " + DateTime.today(TimeZone.getDefault()).plusDays(Integer.valueOf(90)).format("YYYY-MM-DD"));
    }
}
