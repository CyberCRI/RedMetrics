package org.cri.redmetrics.json;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.cri.redmetrics.model.Event;
import java.util.Collection;

public class EventJsonConverter extends ProgressDataJsonConverter<Event> {

    @Inject
    EventJsonConverter(@Named("ProgressData") Gson gson, JsonParser jsonParser) {
        super(Event.class, gson, jsonParser);
    }

    @Override
    public Event parse(String json) {
        Event event = super.parse(json);
        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();

        // TYPE
        JsonElement type = jsonObject.get("type");
        if (type != null) {
            event.setType(type.getAsString().trim().toLowerCase());
        }

        // Coordinates
//        JsonElement coordinates = jsonObject.get("coordinates");
//        if (coordinates != null && coordinates.isJsonArray()) {
//            event.setCoordinates(coordinates.toString());
//        }

        return event;
    }

    @Override
    public JsonObject toJsonObject(Event event) {
        JsonObject eventJson = super.toJsonObject(event);

        // TYPE
        if (event.getType() != null)
            eventJson.addProperty("type", event.getType());

        return eventJson;
    }

}
