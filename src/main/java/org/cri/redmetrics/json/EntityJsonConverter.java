package org.cri.redmetrics.json;

import com.google.gson.*;
import lombok.RequiredArgsConstructor;
import org.cri.redmetrics.model.Entity;
import org.cri.redmetrics.model.ResultsPage;

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
        return gson.toJsonTree(entity).getAsJsonObject();
    }

    public final String stringify(E entity) {
        return gson.toJson(toJsonObject(entity));
    }

    @Override
    public final String render(Object model) {
        if (model instanceof ResultsPage) {
            return stringifyResultsPage((ResultsPage<E>) model);
        } else {
            return stringify((E) model);
        }
    }

    private String stringifyResultsPage(ResultsPage<E> resultsPage) {
        JsonArray jsonArray = new JsonArray();
        resultsPage.results.forEach((entity) -> jsonArray.add(toJsonObject(entity)));
        return gson.toJson(jsonArray);
    }

}
