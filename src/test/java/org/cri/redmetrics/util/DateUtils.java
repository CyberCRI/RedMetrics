package org.cri.redmetrics.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

public class DateUtils {

    private static final DateTimeFormatter RFC1123_DATE_TIME_FORMATTER =
            DateTimeFormat.forPattern("EEE, dd MMM yyyy HH:mm:ss 'UTC'").withLocale(Locale.US);


    public static String print(DateTime date) {
        return RFC1123_DATE_TIME_FORMATTER.print(date);
    }

    public static String now() {
        return print(new DateTime());
    }
}
