package org.cri.redmetrics.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Date;
import java.util.Locale;

public class DateFormatter {

    // ISO 8601 Extended Format
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(DateTimeZone.UTC);

    public static Date parse(String date) {
        return DATE_FORMATTER.parseDateTime(date).toDate();
    }

    public static String print(Date date) {
        return DATE_FORMATTER.print(new DateTime(date.getTime()));
    }

}
