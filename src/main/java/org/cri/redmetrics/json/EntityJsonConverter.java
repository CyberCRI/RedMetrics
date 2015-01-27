package org.cri.redmetrics.json;

import com.google.gson.*;

import lombok.RequiredArgsConstructor;
import org.cri.redmetrics.model.Entity;
import java.util.Collection;
import java.lang.reflect.Type;

@RequiredArgsConstructor
abstract class EntityJsonConverter<E extends Entity> implements JsonConverter<E> {

    protected final Class<E> entityType;
    protected final Type listOfEntityType;
    protected final Gson gson;
    protected final JsonParser jsonParser;

    @Override
    public E parse(String json) {
        return gson.fromJson(json, entityType);
    }

    @Override
    public Collection<E> parseCollection(String json) {
        // new ArrayList<E>(){}.getClass().getGenericSuperclass()
        //Type listType = new TypeToken<ArrayList<E>>(){}.getType();
        return gson.fromJson(json, listOfEntityType);
    }

    public JsonObject toJsonObject(E entity) {
        return gson.toJsonTree(entity).getAsJsonObject();
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
        collection.forEach((entity) -> jsonArray.add(toJsonObject(entity)));
        return gson.toJson(jsonArray);
    }

}
