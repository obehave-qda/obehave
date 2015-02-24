package org.obehave.android.ui.util;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;

/**
 * Created by patrick on 22.02.2015.
 */
final public class DateTimeHelper {

    public static String formatToTimer(DateTime start, DateTime end){
        Period period = new Period(start, end);
        Duration duration = period.toStandardDuration();
        long seconds = duration.getStandardSeconds();
        long minutes = duration.getStandardMinutes();
        seconds -= minutes * 60;
        return "" + ((minutes > 9)?minutes:"0" + minutes) + ":" + ((seconds > 9)?seconds:"0" + seconds);

    }
}
