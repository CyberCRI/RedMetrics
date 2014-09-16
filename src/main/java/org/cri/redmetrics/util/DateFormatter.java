package org.cri.redmetrics.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.Locale;

public class DateFormatter {

    private static final DateTimeFormatter RFC1123_DATE_TIME_FORMATTER =
            DateTimeFormat.forPattern("EEE, dd MMM yyyy HH:mm:ss 'UTC'").withLocale(Locale.US);

    public static Date parse(String date) {
        return RFC1123_DATE_TIME_FORMATTER.parseDateTime(date).toDate();
    }

    public static String print(Date date) {
        return RFC1123_DATE_TIME_FORMATTER.print(new DateTime(date.getTime()));
    }

}
