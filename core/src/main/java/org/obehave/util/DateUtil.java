package org.obehave.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * Factory class that supports creation of legacy {@link java.util.Date} instances<br/>
 * from the JDK8 {@link java.time} classes.
 */
public final class DateUtil {

    /**
     * Creates a {@link java.util.Date} from the given {@link java.time.LocalDate}<br/>
     * setting the time information to the start of the day
     *
     * @param localDate
     * @return date
     */
    public static Date from(LocalDate localDate) {

        return from(localDate.atStartOfDay());
    }

    /**
     * Creates a {@link java.util.Date} from the given {@link java.time.LocalTime}<br/>
     * setting the date information to the current day
     *
     * @param localTime
     * @return date
     */
    public static Date from(LocalTime localTime) {

        return from(localTime.atDate(LocalDate.now()));
    }

    /**
     * Creates a {@link java.util.Date} from the given {@link java.time.LocalDateTime}<br/>
     * setting the time-zone to the systems default value
     *
     * @param localDateTime
     * @return date
     */
    public static Date from(LocalDateTime localDateTime) {

        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Creates a {@link java.util.Date} from the given {@link java.time.Year}<br/>
     * assuming the first day of the year
     *
     * @param year
     * @return date
     */
    public static Date from(Year year) {

        return from(year, TemporalAdjusters.firstDayOfYear());
    }

    /**
     * Creates a {@link java.util.Date} from the given {@link java.time.Year}<br/>
     * assuming the concrete point in time is adjusted by the given {@link TemporalAdjuster}
     *
     * @param year
     * @param adjuster
     * @return date
     */
    public static Date from(Year year, TemporalAdjuster adjuster) {

        return from(year.with(adjuster));
    }

    /**
     * Creates a {@link java.util.Date} from the given {@link java.time.YearMonth}<br/>
     * assuming the first day of the month
     *
     * @return date
     */
    public static Date from(YearMonth yearMonth) {

        return from(yearMonth, TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * Creates a {@link java.util.Date} from the given {@link java.time.YearMonth}<br/>
     * assuming the concrete point in time is adjusted by the given {@link TemporalAdjuster}
     *
     * @param adjuster
     * @return date
     */
    public static Date from(YearMonth yearMonth, TemporalAdjuster adjuster) {

        return from(yearMonth.with(adjuster));
    }

    public static java.sql.Date toSqlDate(LocalDateTime ldt) {
        return new java.sql.Date(java.util.Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant()).getTime());
    }
}