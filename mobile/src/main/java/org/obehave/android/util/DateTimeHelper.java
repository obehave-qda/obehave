package org.obehave.android.util;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;

final public class DateTimeHelper {

    public static String formatToTimer(DateTime start, DateTime end){
        Period period = new Period(start, end);
        Duration duration = period.toStandardDuration();
        long seconds = duration.getStandardSeconds();
        long minutes = duration.getStandardMinutes();
        seconds -= minutes * 60;
        return "" + ((minutes > 9)?minutes:"0" + minutes) + ":" + ((seconds > 9)?seconds:"0" + seconds);
    }

    public static String formatToTimeStr(Long tmstp){
        DateTime dateTime = new DateTime(tmstp);
        return "" + dateTime.getYear() + "-" + dateTime.getMonthOfYear() + "-" + dateTime.getDayOfMonth() + " " + dateTime.getHourOfDay() + ":" + dateTime.getMinuteOfHour();
    }

    public static long diffMs(DateTime startTime, DateTime endTime){
        Period period = new Period(startTime, endTime);
        Duration duration = period.toStandardDuration();
        return duration.getMillis();
    }
}
