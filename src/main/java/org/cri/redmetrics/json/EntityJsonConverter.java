package org.cri.redmetrics.json;

import com.google.gson.*;
import lombok.RequiredArgsConstructor;
import org.cri.redmetrics.model.Entity;
import org.cri.redmetrics.model.ResultsPage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
abstract class EntityJsonConverter<E extends Entity> implements JsonConverter<E> {

    protected final Class<E> entityType;
    protected final Gson gson;
    protected final JsonParser jsonParser;

    @Override
    public E parse(String json) {
        return gson.fromJson(json, entityType);
    }

    @Override
    public Collection<E> parseCollection(String json) {
        JsonElement jsonArrayElement = new JsonParser().parse(json);
        JsonArray jsonArray = jsonArrayElement.getAsJsonArray();

        List<E> list = new ArrayList<E>();
        for(int i = 0; i < jsonArray.size(); i++) {
            JsonElement jsonElement = jsonArray.get(i);
            String jsonString = jsonElement.toString();
            E javaElement = parse(jsonString);
            list.add(javaElement);
        }

        return list;
    }

    public JsonObject toJsonObject(E entity) {
        return gson.toJsonTree(entity).getAsJsonObject();
    }

    public final String stringify(E entity) {
        return gson.toJson(toJsonObject(entity));
    }

    @Override
    public final String render(Object model) {
        if (model instanceof ResultsPage) {
            return stringifyResultsPage((ResultsPage<E>) model);
        } else if(entityType.isInstance(model)) {
            return stringify((E) model);
        } else {
            // Serialize generically (e.g. used for lists of entity IDs)
            return gson.toJson(model);
        }
    }

    private String stringifyResultsPage(ResultsPage<E> resultsPage) {
        JsonArray jsonArray = new JsonArray();
        resultsPage.results.forEach((entity) -> jsonArray.add(toJsonObject(entity)));
        return gson.toJson(jsonArray);
    }

}
