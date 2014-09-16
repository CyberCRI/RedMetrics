package org.cri.redmetrics.json;

import com.google.gson.*;
import org.cri.redmetrics.util.DateFormatter;

import java.lang.reflect.Type;
import java.util.Date;

class DefaultGsonBuilder {

    private static final JsonSerializer<Date> DATE_SERIALIZER = (Date date, Type typeOfSrc, JsonSerializationContext context) -> {
        if (date == null) return null;
        else return new JsonPrimitive(DateFormatter.print(date));
    };

    private static final JsonDeserializer<Date> DATE_DESERIALIZER = (JsonElement json, Type typeOfT, JsonDeserializationContext context) -> {
        if (json == null) return null;
        else return DateFormatter.parse(json.getAsString());
    };

    static GsonBuilder get() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Date.class, DATE_SERIALIZER)
                .registerTypeAdapter(Date.class, DATE_DESERIALIZER);
    }

}
