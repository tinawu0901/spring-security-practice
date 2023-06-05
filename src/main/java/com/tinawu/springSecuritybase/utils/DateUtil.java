package com.tinawu.springSecuritybase.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static Timestamp addMinute(final Timestamp date, final int increment) {
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(date);
        dateCal.add(Calendar.MINUTE, increment);
        return new Timestamp(dateCal.getTime().getTime());
    }

    public static Timestamp addHour(final Timestamp date, final int increment) {
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(date);
        dateCal.add(Calendar.HOUR, increment);
        return new Timestamp(dateCal.getTime().getTime());
    }

    public static Timestamp addDate(final Timestamp date, final int increment) {
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(date);
        dateCal.add(Calendar.DATE, increment);
        return new Timestamp(dateCal.getTime().getTime());
    }

    public static Integer getDayOfWeek(final Date date) {
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(date);
        return dateCal.get(Calendar.DAY_OF_WEEK);
    }

    public static String convertToString(final Date date, final String formatString) {
        final DateFormat dateFormat = new SimpleDateFormat(formatString);
        return dateFormat.format(date);
    }
}
