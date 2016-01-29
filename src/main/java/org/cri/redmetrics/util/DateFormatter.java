package org.cri.redmetrics.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public class DateFormatter {

    // ISO 8601 Extended Format
    private static final DateTimeFormatter ISO_DATE_FORMATTER =
            DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(DateTimeZone.UTC);

    private static final DateTimeFormatter DB_DAY_DATE_FORMATTER =
            DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").withZone(DateTimeZone.UTC);

    public static Date parseIso(String date) {
        return ISO_DATE_FORMATTER.parseDateTime(date).toDate();
    }

    public static String printIso(Date date) {
        return ISO_DATE_FORMATTER.print(new DateTime(date.getTime()));
    }

    public static Date parseDbDay(String date) {
        return DB_DAY_DATE_FORMATTER.parseDateTime(date).toDate();
    }

    public static String printDbDay(Date date) {
        return DB_DAY_DATE_FORMATTER.print(new DateTime(date.getTime()));
    }

}
