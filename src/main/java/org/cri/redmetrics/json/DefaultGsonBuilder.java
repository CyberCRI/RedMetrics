package org.cri.redmetrics.json;

import com.google.gson.*;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Locale;

class DefaultGsonBuilder {

    private static final DateTimeFormatter RFC1123_DATE_TIME_FORMATTER =
            DateTimeFormat.forPattern("EEE, dd MMM yyyy HH:mm:ss 'UTC'").withLocale(Locale.US);

    private static final JsonSerializer<Date> DATE_SERIALIZER = (Date date, Type typeOfSrc, JsonSerializationContext context) -> {
        if (date == null) return null;
        else return new JsonPrimitive(RFC1123_DATE_TIME_FORMATTER.print(new DateTime(date.getTime())));
    };

    private static final JsonDeserializer<Date> DATE_DESERIALIZER = (JsonElement json, Type typeOfT, JsonDeserializationContext context) -> {
        if (json == null) return null;
        else return RFC1123_DATE_TIME_FORMATTER.parseDateTime(json.getAsString()).toDate();
    };

    static GsonBuilder get() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Date.class, DATE_SERIALIZER)
                .registerTypeAdapter(Date.class, DATE_DESERIALIZER);
    }

}
