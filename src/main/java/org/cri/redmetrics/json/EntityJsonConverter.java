package org.cri.redmetrics.json;

import com.google.gson.*;
import lombok.RequiredArgsConstructor;
import org.cri.redmetrics.model.BinCount;
import org.cri.redmetrics.model.Entity;
import org.cri.redmetrics.model.ResultsPage;
import org.cri.redmetrics.util.RouteHelper;

import java.util.*;

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

    // TODO: remove this unused method?
    @Override
    public final String render(Object model) {
        return gson.toJson(model);
    }

    public String render(RouteHelper.DataType dataType, Object data) {
        switch(dataType) {
            case ENTITY:
                return renderEntity((E) data);
            case ENTITY_LIST_OR_RESULTS_PAGE:
                if(data instanceof List)
                    return renderEntityList((List<E>) data);
                else
                    return renderResultsPage((ResultsPage<E>) data);
            case ENTITY_OR_ID_LIST:
                if(data instanceof Entity) {
                    return renderEntity((E) data);
                } else {
                    return renderIdList(data);
                }

            case BIN_COUNT_LIST:
                return renderBinCounts((List<BinCount>) data);
        }

        throw new RuntimeException("Cannot handle dataType ");
    }

    private String renderResultsPage(ResultsPage<E> resultsPage) {
        JsonArray jsonArray = new JsonArray();
        resultsPage.results.forEach((entity) -> jsonArray.add(toJsonObject(entity)));
        return gson.toJson(jsonArray);
    }

    private String renderBinCounts(List<BinCount> binCounts) {
        JsonArray jsonArray = new JsonArray();
        binCounts.forEach((binCount) -> jsonArray.add(gson.toJsonTree(binCount).getAsJsonObject()));
        return gson.toJson(jsonArray);
    }

    private String renderEntityList(List<E> results) {
        JsonArray jsonArray = new JsonArray();
        results.forEach((entity) -> jsonArray.add(toJsonObject(entity)));
        return gson.toJson(jsonArray);
    }

    private String renderEntity(E model) {
        return stringify(model);
    }

    private String renderIdList(Object idList) {
        return gson.toJson(idList);
    }

}
