package org.cri.redmetrics.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

public class DateUtils {

    // ISO 8601 Extended Format
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(DateTimeZone.UTC);

    public static String print(DateTime date) {
        return DATE_FORMATTER.print(date);
    }

    public static String now() {
        return print(new DateTime());
    }

}
