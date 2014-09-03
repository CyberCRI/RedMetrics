package org.cri.redmetrics.json;

import com.google.gson.*;
import lombok.RequiredArgsConstructor;
import org.cri.redmetrics.model.Entity;

import java.util.Collection;

@RequiredArgsConstructor
abstract class EntityJsonConverter<E extends Entity> implements JsonConverter<E> {

    protected final Class<E> entityType;
    protected final Gson gson;
    protected final JsonParser jsonParser;

    @Override
    public E parse(String json) {
        return gson.fromJson(json, entityType);
    }

    public JsonObject toJsonObject(E entity) {
        JsonElement element = gson.toJsonTree(entity);
        if (element.isJsonObject()) return element.getAsJsonObject();
        else return null;
    }

    public final String stringify(E entity) {
        return gson.toJson(toJsonObject(entity));
    }

    @Override
    public final String render(Object model) {
        if (model instanceof Collection) {
            return stringifyCollection((Collection<E>) model);
        } else {
            return stringify((E) model);
        }
    }

    private String stringifyCollection(Collection<E> collection) {
        JsonArray jsonArray = new JsonArray();
        for (E entity : collection) {
            jsonArray.add(toJsonObject(entity));
        }
        return gson.toJson(jsonArray);
    }

}
